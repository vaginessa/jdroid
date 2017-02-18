#!/bin/sh
set -e

# The path to a directory where the code will be checked out and the assemblies would be generated. For example: /home/user/build. Required.
BUILD_DIRECTORY=$1
GIT_HUB_TOKEN=$2
GIT_HUB_READ_ONLY_TOKEN=$3
GIT_HUB_EMAIL=$4

BUILD_SAMPLES=false

# Whether the source code and assemblies on the build directory should be cleaned or not
CLEAN=false

REPOSITORY_OWNER=maxirosson
PROJECT_NAME=jdroid

PROJECT_DIRECTORY=$BUILD_DIRECTORY/$PROJECT_NAME
SOURCE_DIRECTORY=$PROJECT_DIRECTORY/sources
PROJECT_HOME=$SOURCE_DIRECTORY/$PROJECT_NAME

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
	echo Cloning git@github.com:$REPOSITORY_OWNER/$PROJECT_NAME.git
	git clone git@github.com:$REPOSITORY_OWNER/$PROJECT_NAME.git $PROJECT_NAME
fi
cd $PROJECT_HOME
git config user.email $GIT_HUB_EMAIL

# ************************
# Synch production branch
# ************************

git add -A
git stash
git co production
git pull

VERSION=`./gradlew :printVersion -q --configure-on-demand -PSNAPSHOT=false`

# ************************
# Close Milestone on GitHub
# ************************

./gradlew :closeGitHubMilestone --configure-on-demand -PSNAPSHOT=false -PRELEASE_BUILD_TYPE_ENABLED=true -PREPOSITORY_OWNER=$REPOSITORY_OWNER -PREPOSITORY_NAME=$PROJECT_NAME -PGITHUB_OATH_TOKEN=$GIT_HUB_TOKEN

read -p "Verify that the milestone is closed on Milestones [https://github.com/$REPOSITORY_OWNER/$PROJECT_NAME/milestones] and press [Enter] key to continue..."

# ************************
# Upload Release on GitHub
# ************************

./gradlew :createGitHubRelease --configure-on-demand -PSNAPSHOT=false -PRELEASE_BUILD_TYPE_ENABLED=true -PREPOSITORY_OWNER=$REPOSITORY_OWNER -PREPOSITORY_NAME=$PROJECT_NAME -PGITHUB_OATH_TOKEN=$GIT_HUB_TOKEN

read -p "Verify that the release is present on Releases [https://github.com/$REPOSITORY_OWNER/$PROJECT_NAME/releases] and press [Enter] key to continue..."

# ************************
# Generate Change Log
# ************************

github_changelog_generator --no-unreleased --no-pull-requests --no-pr-wo-labels --exclude-labels task -t $GIT_HUB_READ_ONLY_TOKEN

git add CHANGELOG.md
git commit -m "Updated CHANGELOG.md"
git diff HEAD

read -p "Please verify the $PROJECT_HOME/CHANGELOG.md and press [Enter] key to continue..."

git push origin HEAD:production

# ************************
# Deploy to Sonatype repository
# ************************

cd $PROJECT_HOME

cmd="./gradlew clean"

cmd="${cmd} :jdroid-java:uploadArchives"
cmd="${cmd} :jdroid-java-http-okhttp:uploadArchives"
cmd="${cmd} :jdroid-java-firebase-database:uploadArchives"

cmd="${cmd} :jdroid-android:uploadArchives"
cmd="${cmd} :jdroid-android-about:uploadArchives"
cmd="${cmd} :jdroid-android-crashlytics:uploadArchives"
cmd="${cmd} :jdroid-android-facebook:uploadArchives"
cmd="${cmd} :jdroid-android-firebase-database:uploadArchives"
cmd="${cmd} :jdroid-android-firebase-fcm:uploadArchives"
cmd="${cmd} :jdroid-android-firebase-remoteconfig:uploadArchives"
cmd="${cmd} :jdroid-android-firebase-admob:uploadArchives"
cmd="${cmd} :jdroid-android-firebase-crash:uploadArchives"
cmd="${cmd} :jdroid-android-google-inappbilling:uploadArchives"
cmd="${cmd} :jdroid-android-google-maps:uploadArchives"
cmd="${cmd} :jdroid-android-google-plus:uploadArchives"
cmd="${cmd} :jdroid-android-google-signin:uploadArchives"
cmd="${cmd} :jdroid-android-twitter:uploadArchives"
cmd="${cmd} :jdroid-android-about:uploadArchives"
cmd="${cmd} -PSNAPSHOT=false -PLOCAL_UPLOAD=false -PRELEASE_BUILD_TYPE_ENABLED=true -PNO_SNAPSHOT_VALIDATION_ENABLED=true"

echo "Executing the following command"
echo "${cmd}"
eval "${cmd}"
