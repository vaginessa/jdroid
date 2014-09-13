#!/bin/sh

JDROID_HOME=$1
PROJECT_PATH=$2
APP_TYPE=$3
VERSION_TYPE=$4

if [ -z "$VERSION_TYPE" ]
then
	echo "[ERROR] The VERSION_TYPE parameter is required"
	exit 1;
fi

sh $JDROID_HOME/jdroid-scripts/incrementPomVersion.sh $JDROID_HOME $PROJECT_PATH $VERSION_TYPE true 'version' false $APP_TYPE
