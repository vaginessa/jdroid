#!/bin/sh

SOURCE_DIRECTORY=$1
VERSION_TYPE=$2
SNAPSHOT=$3

# Change version
# ************************

sh $SOURCE_DIRECTORY/jdroid-scripts/incrementPomVersion.sh $SOURCE_DIRECTORY $SOURCE_DIRECTORY $VERSION_TYPE false 'version' $SNAPSHOT
sh $SOURCE_DIRECTORY/jdroid-scripts/incrementPomVersion.sh $SOURCE_DIRECTORY $SOURCE_DIRECTORY/jdroid-java $VERSION_TYPE false 'version' $SNAPSHOT
sh $SOURCE_DIRECTORY/jdroid-scripts/incrementPomVersion.sh $SOURCE_DIRECTORY $SOURCE_DIRECTORY/jdroid-android $VERSION_TYPE false 'version' $SNAPSHOT
sh $SOURCE_DIRECTORY/jdroid-scripts/incrementPomVersion.sh $SOURCE_DIRECTORY $SOURCE_DIRECTORY/jdroid-support-v7-appcompat $VERSION_TYPE false 'version' $SNAPSHOT
sh $SOURCE_DIRECTORY/jdroid-scripts/incrementPomVersion.sh $SOURCE_DIRECTORY $SOURCE_DIRECTORY/jdroid-google-play-services $VERSION_TYPE false 'version' $SNAPSHOT
sh $SOURCE_DIRECTORY/jdroid-scripts/incrementPomVersion.sh $SOURCE_DIRECTORY $SOURCE_DIRECTORY/jdroid-javaweb $VERSION_TYPE false 'version' $SNAPSHOT

sh $SOURCE_DIRECTORY/jdroid-scripts/incrementPomVersion.sh $SOURCE_DIRECTORY $SOURCE_DIRECTORY/jdroid-sample-server $VERSION_TYPE false 'version' $SNAPSHOT
sh $SOURCE_DIRECTORY/jdroid-scripts/incrementPomVersion.sh $SOURCE_DIRECTORY $SOURCE_DIRECTORY/jdroid-sample-server $VERSION_TYPE false 'jdroid.version' $SNAPSHOT

sh $SOURCE_DIRECTORY/jdroid-scripts/android/incrementAndroidAppVersion.sh $SOURCE_DIRECTORY $SOURCE_DIRECTORY/jdroid-sample-android $VERSION_TYPE 'version' $SNAPSHOT false
sh $SOURCE_DIRECTORY/jdroid-scripts/incrementPomVersion.sh $SOURCE_DIRECTORY $SOURCE_DIRECTORY/jdroid-sample-android $VERSION_TYPE true 'jdroid.version' $SNAPSHOT 'jdroid'
