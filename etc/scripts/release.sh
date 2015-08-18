#!/bin/sh
set -e

# The path to a directory where the code will be checked out and the assemblies would be generated. For example: /home/user/build. Required.
BUILD_DIRECTORY=$1
GIT_HUB_TOKEN=$2

BUILD_SAMPLES=false

# Whether the source code and assemblies on the build directory should be cleaned or not
CLEAN=false

PROJECT_DIRECTORY=$BUILD_DIRECTORY/jdroid
SOURCE_DIRECTORY=$PROJECT_DIRECTORY/sources
ASSEMBLIES_DIRECTORY=$PROJECT_DIRECTORY/assemblies
PROJECT_HOME=$SOURCE_DIRECTORY/jdroid

# ************************
# Parameters validation
# ************************

if [ -z "$BUILD_DIRECTORY" ]
then
	echo "[ERROR] The BUILD_DIRECTORY parameter is required"
	exit 1;
fi

if [ ! -d "$BUILD_DIRECTORY" ]
then
	echo "[ERROR] - The BUILD_DIRECTORY directory does not exist."
	exit 1;
fi

if [ -z "$GIT_HUB_TOKEN" ]
then
	echo "[ERROR] The GIT_HUB_TOKEN parameter is required"
	exit 1;
fi

# ************************
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
	git clone git@github.com:maxirosson/jdroid.git jdroid
fi


# ************************
# Synch production branch
# ************************

cd $PROJECT_HOME
git add -A
git stash
git checkout production
git pull

VERSION=`./gradlew :printVersion -q --configure-on-demand -PSNAPSHOT=false`

# ************************
# Samples Assemblies Generation
# ************************

if [ "$BUILD_SAMPLES" = "true" ]
then

    # Cleaning the assemblies
    rm -r -f $ASSEMBLIES_DIRECTORY
    mkdir -p $ASSEMBLIES_DIRECTORY

	# Generate the jdroid sample server war
	cd $PROJECT_HOME/jdroid-sample-server
	mvn clean dependency:resolve -P $PROFILE assembly:assembly -Dmaven.test.skip=true
	cp ./target/*.war $ASSEMBLIES_DIRECTORY/

	# Generate the jdroid sample android apk
	ANDROID_APP_DIR=$PROJECT_HOMEjdroid-sample-android
	cd $ANDROID_APP_DIR
	mvn clean dependency:resolve -P $PROFILE install -Dmaven.test.skip=true
	cp ./target/*.apk $ASSEMBLIES_DIRECTORY/
	sh $JDROID_HOME/jdroid-scripts/android/validateDex.sh $ANDROID_APP_DIR/target/classes.dex
fi

# ************************
# Deploy to Sonatype repository
# ************************

cd $PROJECT_HOME

./gradlew :jdroid-java:clean :jdroid-java:uploadArchives :jdroid-gradle-plugin:clean :jdroid-gradle-plugin:uploadArchives --configure-on-demand -PLOCAL_UPLOAD=true -PSNAPSHOT=false
./gradlew clean :jdroid-gradle-plugin:uploadArchives :jdroid-java:uploadArchives :jdroid-java-http-apache:uploadArchives :jdroid-java-http-urlconnection:uploadArchives :jdroid-java-http-okhttp:uploadArchives :jdroid-javaweb:uploadArchives :jdroid-android:uploadArchives :jdroid-android-facebook:uploadArchives :jdroid-android-google-maps:uploadArchives :jdroid-android-google-gcm:uploadArchives :jdroid-android-google-plus:uploadArchives :jdroid-android-about:uploadArchives -PSNAPSHOT=false

# ************************
# Publish the Gradle Plugin to the Portal
# ************************

cd $PROJECT_HOME
./gradlew :jdroid-gradle-plugin:clean :jdroid-gradle-plugin:publishPlugins --configure-on-demand -PSNAPSHOT=false -PSIGNING_ENABLED=false

# ************************
# Upload Release on GitHub
# ************************

./gradlew :createGitHubRelease --configure-on-demand -PSNAPSHOT=false -PGITHUB_OATH_TOKEN=$GIT_HUB_TOKEN

# ************************
# Close Milestone on GitHub
# ************************

./gradlew :closeGitHubMilestone --configure-on-demand -PSNAPSHOT=false -PGITHUB_OATH_TOKEN=$GIT_HUB_TOKEN

# ************************
# Update the Jdroid GitHub page
# ************************

cd $PROJECT_HOME
git add -A
git stash
git checkout gh-pages
git add -A
git stash
git pull

sed -i '' 's/v[0-9].[0-9].[0-9]/v'$VERSION'/g' index.html
git add index.html
git commit -m 'Updated jdroid version to v'$VERSION
git push origin HEAD:gh-pages

