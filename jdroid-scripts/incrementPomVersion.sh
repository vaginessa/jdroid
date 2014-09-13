#!/bin/sh

JDROID_HOME=$1
PROJECT_PATH=$2
VERSION_TYPE=$3
COMMIT=$4
VERSION_TAG=$5
SNAPSHOT=$6
APP_TYPE=$7

if [ -z "$VERSION_TYPE" ]
then
	echo "[ERROR] The VERSION_TYPE parameter is required"
	exit 1;
fi

if [ -z "$COMMIT" ]
then
	COMMIT="true"
fi

if [ -z "$VERSION_TAG" ]
then
	VERSION_TAG="version"
fi

if [ -z "$SNAPSHOT" ]
then
	SNAPSHOT="false"
fi

POM_PATH=$PROJECT_PATH/pom.xml

currentVersion=`grep -m 1 "<$VERSION_TAG>.*<.$VERSION_TAG>" $POM_PATH | sed -e "s/^.*<$VERSION_TAG/<$VERSION_TAG/" | cut -f2 -d">"| cut -f1 -d"<"`
echo "The current version is $currentVersion"
currentVersion=`echo $currentVersion | sed -e "s/-SNAPSHOT//"`

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
	minor='0'
	patch='0'
fi

if [ "$VERSION_TYPE" = "minor" ]
then
	minor=$((minor+1))
	patch='0'
fi

if [ "$VERSION_TYPE" = "patch" ]
then
	patch=$((patch+1))
fi

VERSION=$major.$minor.$patch
if [ "$SNAPSHOT" = "true" ]
then
	VERSION=$VERSION"-SNAPSHOT"
fi

echo "The new version is $VERSION"

sh $JDROID_HOME/jdroid-scripts/replaceXmlTag.sh $POM_PATH $VERSION_TAG $VERSION

if [ "$COMMIT" = "true" ]
then
	git diff HEAD
	git add -A
	git commit -m "Changed $APP_TYPE version to v$VERSION"
fi
