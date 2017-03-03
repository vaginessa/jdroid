#!/bin/sh

# Note the set -ev at the top. The -e flag causes the script to exit as soon as one command
# returns a non-zero exit code. The -v flag makes the shell print all lines in the script
# before executing them, which helps identify which steps failed.
set -e

LOCAL_UPLOAD=$1
DEBUG=$2

if [ -z "$LOCAL_UPLOAD" ]
then
	LOCAL_UPLOAD="true"
fi

cmd="./gradlew clean assemble testDebug lintDebug :jdroid-android-sample:assembleUat :jdroid-android-sample:testDebug :jdroid-android-sample:lintDebug uploadArchives -PLOCAL_UPLOAD=$LOCAL_UPLOAD -PRELEASE_BUILD_TYPE_ENABLED=true -PRELEASE_FAKE_ENABLED=true --configure-on-demand --refresh-dependencies --stacktrace"

if [ "$DEBUG" = "true" ]
then
	cmd="${cmd} --debug"
fi

echo "Executing the following command"
echo "${cmd}"
eval "${cmd}"
