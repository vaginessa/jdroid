#!/bin/sh

# Note the set -ev at the top. The -e flag causes the script to exit as soon as one command 
# returns a non-zero exit code. The -v flag makes the shell print all lines in the script 
# before executing them, which helps identify which steps failed.
set -e

INSTALL=$1

if [ -z "$INSTALL" ]
then
	echo "[ERROR] The INSTALL parameter is required"
	echo "Run the script with '-h' for help"
	exit 1;
fi

# ************************
# jdroid
# ************************

mvn clean install -P jdroid-test

# Validate localizations
VALIDATE_LOCALIZATIONS=./jdroid-scripts/android/validateLocalizations.sh
sh $VALIDATE_LOCALIZATIONS 'string name=' './jdroid-android/res/values/strings.xml' './jdroid-android/res/values-es/strings.xml'

# Missing translations validation
VALIDATE_MISSING=./jdroid-scripts/android/validateMissingTranslations.sh
sh $VALIDATE_MISSING './jdroid-android/res/values/strings.xml'
sh $VALIDATE_MISSING './jdroid-android/res/values-es/strings.xml'
sh $VALIDATE_MISSING './jdroid-sample-android/res/values/strings.xml'
sh $VALIDATE_MISSING './jdroid-sample-android/res/values-es/strings.xml'


if [ "$INSTALL" = "true" ]
then
	mvn clean install -P jdroid-release -Dmaven.test.skip=true -Dgpg.skip=true
	#mvn clean deploy -P jdroid-release -Dmaven.test.skip=true -Dgpg.skip=true --settings ./settings.xml
fi

# ************************
# jdroid sample android
# ************************

cd jdroid-sample-android
mvn clean test -P jdroid-test

if [ "$INSTALL" = "true" ]
then
	mvn clean install -P jdroid-sample-android-uat -Dmaven.test.skip=true --settings ../settings.xml
fi

# ************************
# jdroid sample server
# ************************

cd ../jdroid-sample-server
mvn clean test -P jdroid-test

if [ "$INSTALL" = "true" ]
then
	mvn clean assembly:assembly -P jdroid-sample-server-uat -Dmaven.test.skip=true --settings ../settings.xml
fi
