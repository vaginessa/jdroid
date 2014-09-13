#!/bin/sh

JDROID_HOME=$1
PROJECT_PATH=$2
VERSION_TYPE=$3
VERSION_TAG=$4
SNAPSHOT=$5
COMMIT=$6

if [ -z "$VERSION_TYPE" ]
then
	echo "[ERROR] The VERSION_TYPE parameter is required"
	exit 1;
fi

if [ -z "$VERSION_TAG" ]
then
	VERSION_TAG="version"
fi

if [ -z "$SNAPSHOT" ]
then
	SNAPSHOT="false"
fi

if [ -z "$COMMIT" ]
then
	COMMIT="true"
fi

sh $JDROID_HOME/jdroid-scripts/android/incrementManifestVersion.sh $JDROID_HOME $PROJECT_PATH $VERSION_TYPE true
sh $JDROID_HOME/jdroid-scripts/incrementPomVersion.sh $JDROID_HOME $PROJECT_PATH $VERSION_TYPE $COMMIT $VERSION_TAG $SNAPSHOT 'android app'
