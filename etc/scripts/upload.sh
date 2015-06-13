#!/bin/sh

# Note the set -ev at the top. The -e flag causes the script to exit as soon as one command
# returns a non-zero exit code. The -v flag makes the shell print all lines in the script
# before executing them, which helps identify which steps failed.
set -e

LOCAL_UPLOAD=$1

if [ -z "$LOCAL_UPLOAD" ]
then
	LOCAL_UPLOAD="true"
fi

cd $JDROID_HOME
./gradlew :jdroid-gradle-plugin:uploadArchives --configure-on-demand -PLOCAL_UPLOAD=$LOCAL_UPLOAD
./gradlew :jdroid-gradle-plugin:uploadArchives :jdroid-java:uploadArchives :jdroid-javaweb:uploadArchives :jdroid-android:uploadArchives -PLOCAL_UPLOAD=$LOCAL_UPLOAD --refresh-dependencies
