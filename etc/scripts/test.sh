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

./gradlew build

# ************************
# jdroid sample android
# ************************

cd jdroid-sample-android
../gradlew build




#if [ "$INSTALL" = "true" ]
#then
	#mvn clean install -P jdroid-release -Dmaven.test.skip=true -Dgpg.skip=true
#	mvn clean deploy -P jdroid-release -Dmaven.test.skip=true -Dgpg.skip=true --settings ./settings.xml
#fi

# ************************
# jdroid sample android
# ************************

#cd jdroid-sample-android
#mvn clean test -P jdroid-test

#if [ "$INSTALL" = "true" ]
#then
#	mvn clean install -P jdroid-sample-android-uat -Dmaven.test.skip=true --settings ../settings.xml
#fi

# ************************
# jdroid sample server
# ************************

#cd ../jdroid-sample-server
#mvn clean test -P jdroid-test

#if [ "$INSTALL" = "true" ]
#then
#	$MAVEN323_HOME/mvn clean assembly:assembly -P jdroid-sample-server-uat -Dmaven.test.skip=true --settings ../settings.xml
#fi
