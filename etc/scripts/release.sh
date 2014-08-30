#!/bin/sh
set -e

BUILD_DIRECTORY=$1
OAUTH_TOKEN=$2

PROJECT_NAME=jdroid
SOURCE_DIRECTORY=$BUILD_DIRECTORY/$PROJECT_NAME/source/$PROJECT_NAME
ASSEMBLIES_DIRECTORY=$BUILD_DIRECTORY/$PROJECT_NAME/assemblies

# Build
# ************************
sh ./build.sh $BUILD_DIRECTORY

# Javadoc & zip Generation
# ************************
cd $SOURCE_DIRECTORY
mvn clean install javadoc:aggregate assembly:single -P jdroid-release -Dmaven.test.skip=true
cp ./target/*.zip $ASSEMBLIES_DIRECTORY/
cp -r ./target/site $ASSEMBLIES_DIRECTORY

ASSEMBLY_FILE_PATH=""
for filename in $ASSEMBLIES_DIRECTORY/*jdroid.zip
do
	newname=`echo $filename | sed 's/jdroid-parent/jdroid/g' | sed 's/-jdroid\.zip/\.zip/g'`
	mv $filename $newname
	ASSEMBLY_FILE_PATH=$newname
done

# Deploy to Maven repository
# ************************
mvn clean deploy -P jdroid-release -Dmaven.test.skip=true

# Upload Release on GitHub
# ************************

REPO_OWNER=maxirosson
BODY=`cat ./etc/releaseNotes.txt`
TAG_NAME=v`mvn help:evaluate -Dexpression=project.version 2>/dev/null| grep -v "^\["`

RESPONSE=$(curl \
    -X POST \
    -H "Authorization: token $OAUTH_TOKEN" \
    -d@- \
    "https://api.github.com/repos/$REPO_OWNER/$PROJECT_NAME/releases" <<EOF
{
  "tag_name": "$TAG_NAME",
  "target_commitish": "master",
  "name": "$TAG_NAME",
  "draft": false,
  "prerelease": false,
  "body": "$BODY"
}
EOF)

# Upload the jdroid-vX.X.X.zip to the release created on Github.
# ************************

ASSEMBLY_FILE_NAME="$PROJECT_NAME-$TAG_NAME.zip" 

UPLOAD_URL=$(echo $RESPONSE | sed -e "s/^.*\"upload_url\": \"//" | cut -f1 -d"\"" | sed -e "s/{?name}/?name=$ASSEMBLY_FILE_NAME/")
echo "Uploading assembly to $UPLOAD_URL"


curl -H "Authorization: token $OAUTH_TOKEN" \
     -H "Accept: application/vnd.github.manifold-preview" \
     -H "Content-Type: application/zip" \
     --data-binary @$ASSEMBLY_FILE_PATH \
     "$UPLOAD_URL"
     
     
# Update the JavaDocs on gh-pages branch
# ************************

git config user.name "maxirosson"
git config user.email "maxirosson@gmail.com"
git add -A
git stash
git co gh-pages
git add -A
git stash
rm -rf ./javadocs
mkdir javadocs
cp -r $ASSEMBLIES_DIRECTORY/site/apidocs/* ./javadocs/
git co -- ./javadocs/stylesheet.css
git add -A
git commit -m "Upgraded javadocs to $PROJECT_NAME $TAG_NAME"
git push origin HEAD:gh-pages

