#!/bin/sh

JDROID_HOME=$1
PROJECT_PATH=$2
VERSION_TYPE=$3

POM_PATH=$PROJECT_PATH/pom.xml

currentVersion=`grep -m 1 "<version>.*<.version>" $POM_PATH | sed -e "s/^.*<version/<version/" | cut -f2 -d">"| cut -f1 -d"<"`
echo "The current version is $currentVersion"


OIFS=$IFS
set -- "$currentVersion" 
IFS="."
versionItems=($*)
IFS=$OIFS

major="${versionItems[0]}"
minor="${versionItems[1]}" 
patch="${versionItems[2]}" 

if [ "$VERSION_TYPE" = "major" ]
then
	major=$((major+1))
fi

if [ "$VERSION_TYPE" = "minor" ]
then
	minor=$((minor+1))
fi

if [ "$VERSION_TYPE" = "patch" ]
then
	patch=$((patch+1))
fi

VERSION=$major.$minor.$patch

echo "The new version is $VERSION"

sh $JDROID_HOME/jdroid-scripts/replaceXmlTag.sh $POM_PATH 'version' $VERSION

git diff HEAD
git add -A
git commit -m "Changed app version to v$VERSION"
