#!/bin/sh

BUILD_DIRECTORY=$1
OAUTH_TOKEN=$2

PROJECT_NAME=jdroid
SOURCE_DIRECTORY=$BUILD_DIRECTORY/$PROJECT_NAME/source
ASSEMBLIES_DIRECTORY=$BUILD_DIRECTORY/$PROJECT_NAME/assemblies

# Build
# ************************
sh ./build.sh $BUILD_DIRECTORY
if [ $? -ne 0 ]
then
	exit 1
fi

for filename in $ASSEMBLIES_DIRECTORY/*jdroid.zip
do
	newname=`echo $filename | sed 's/jdroid-parent/jdroid/g' | sed 's/-jdroid\.zip/\.zip/g'`
	mv $filename $newname
done


# Javadoc Generation
# ************************
cd $SOURCE_DIRECTORY/$PROJECT_NAME
mvn javadoc:aggregate
if [ $? -ne 0 ]
then
	exit 1
fi

# Deploy to Maven repository
# ************************
mvn deploy assembly:single -Dmaven.test.skip=true
if [ $? -ne 0 ]
then
	exit 1
fi
cp ./target/*.zip $ASSEMBLIES_DIRECTORY/
if [ $? -ne 0 ]
then
	exit 1
fi

# Upload Release on GitHub
# ************************

REPO_OWNER=maxirosson
BODY=`cat ./releaseNotes.txt`
TAG_NAME=v`mvn help:evaluate -Dexpression=project.version 2>/dev/null| grep -v "^\["`

curl \
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
EOF
