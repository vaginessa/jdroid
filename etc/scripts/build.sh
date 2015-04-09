#!/bin/sh
set -e

BUILD_DIRECTORY=$1
CLEAN=$2
BRANCH=$3
BUILD_SAMPLES=$4
DEPLOY=$5
PROFILE=$6

PROJECT_NAME=jdroid

# Help
# ****
if [ $# -eq 1 ] && [ $1 = -h ]
then
        echo "Help"
        echo "****"
        echo ""
        echo "This script will build the application."
        echo "Available parameters"
        echo ""
        echo " 1) The path to a directory where the code will be checked out and the assemblies would be generated. For example: /home/user/build. Required."
        echo ""
        echo " 2) Whether the source code and assemblies on the build directory should be cleaned or not. Required. Default value: true"
        echo ""
        echo " 3) The branch from where check out the code. Optional. Default value: master"
        echo ""
        exit 0
fi

# Parameters validation
# ************************
if [ -z "$BUILD_DIRECTORY" ]
then
	echo "[ERROR] The BUILD_DIRECTORY parameter is required"
	echo "Run the script with '-h' for help"
	exit 1;
fi

if [ ! -d "$BUILD_DIRECTORY" ]
then
	echo "[ERROR] - The BUILD_DIRECTORY directory does not exist."
	echo "Run the script with '-h' for help"
	exit 1;
fi

if [ -z "$CLEAN" ]
then
	CLEAN="true"
fi

if [ -z "$BRANCH" ]
then
	BRANCH=master
fi

if [ -z "$BUILD_SAMPLES" ]
then
	BUILD_SAMPLES="false"
fi

if [ -z "$DEPLOY" ]
then
	DEPLOY="false"
fi

SOURCE_DIRECTORY=$BUILD_DIRECTORY/$PROJECT_NAME/source
ASSEMBLIES_DIRECTORY=$BUILD_DIRECTORY/$PROJECT_NAME/assemblies

# Cleaning the assemblies
# ************************
rm -r -f $ASSEMBLIES_DIRECTORY
mkdir -p $ASSEMBLIES_DIRECTORY

# Checking out
# ************************

if [ "$CLEAN" = "true" ] || [ ! -d "$SOURCE_DIRECTORY" ]
then
	# Clean the directories
	rm -r -f $SOURCE_DIRECTORY
	mkdir -p $SOURCE_DIRECTORY

	# Checkout the project
	cd $SOURCE_DIRECTORY
	echo Cloning git@github.com:maxirosson/jdroid.git
	git clone git@github.com:maxirosson/jdroid.git $PROJECT_NAME
else
	cd $SOURCE_DIRECTORY/$PROJECT_NAME
	git add -A
	git stash
fi

cd $SOURCE_DIRECTORY/$PROJECT_NAME
git checkout $BRANCH

if [ "$CLEAN" = "false" ]
then
	git pull
fi

# Samples Assemblies Generation
# ************************
if [ "$BUILD_SAMPLES" = "true" ]
then

	# Generate the jdroid sample server war
	cd $SOURCE_DIRECTORY/$PROJECT_NAME/jdroid-sample-server
	mvn clean dependency:resolve -P $PROFILE assembly:assembly -Dmaven.test.skip=true
	cp ./target/*.war $ASSEMBLIES_DIRECTORY/

	# Generate the jdroid sample android apk
	ANDROID_APP_DIR=$SOURCE_DIRECTORY/$PROJECT_NAME/jdroid-sample-android
	cd $ANDROID_APP_DIR
	mvn clean dependency:resolve -P $PROFILE install -Dmaven.test.skip=true
	cp ./target/*.apk $ASSEMBLIES_DIRECTORY/
	sh $JDROID_HOME/jdroid-scripts/android/validateDex.sh $ANDROID_APP_DIR/target/classes.dex
fi

# Deploy to Sonatype repository
# ************************
cd $SOURCE_DIRECTORY/$PROJECT_NAME

if [ "$DEPLOY" = "true" ]
then
	./gradlew :jdroid-java:clean :jdroid-java:uploadArchives :jdroid-gradle-plugin:clean :jdroid-gradle-plugin:uploadArchives --configure-on-demand -PLOCAL_UPLOAD=true
	./gradlew clean :jdroid-gradle-plugin:uploadArchives :jdroid-java:uploadArchives :jdroid-javaweb:uploadArchives :jdroid-android:uploadArchives
fi