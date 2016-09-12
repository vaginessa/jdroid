#!/bin/sh

# Note the set -ev at the top. The -e flag causes the script to exit as soon as one command
# returns a non-zero exit code. The -v flag makes the shell print all lines in the script
# before executing them, which helps identify which steps failed.
set -e

UPLOAD=$1
LOCAL_UPLOAD=$2
ENABLE_TESTS=$3
ENABLE_ANDROID=$4
DEBUG=$5

if [ -z "$UPLOAD" ]
then
	UPLOAD="false"
fi

if [ -z "$LOCAL_UPLOAD" ]
then
	LOCAL_UPLOAD="true"
fi

if [ -z "ENABLE_TESTS" ]
then
	ENABLE_TESTS="true"
fi

if [ -z "$ENABLE_JAVA_WEB" ]
then
	ENABLE_JAVA_WEB="true"
fi

if [ -z "$ENABLE_ANDROID" ]
then
	ENABLE_ANDROID="true"
fi

# ************************
# jdroid gradle plugin
# ************************

cmd="./gradlew :jdroid-gradle-plugin:clean :jdroid-gradle-plugin:uploadArchives --configure-on-demand -PLOCAL_UPLOAD=true --stacktrace"
if [ "$DEBUG" = "true" ]
then
	cmd="${cmd} --debug"
fi

echo "Executing the following command"
echo "${cmd}"
eval "${cmd}"

cmd="./gradlew clean"
if [ "$ENABLE_TESTS" = "true" ]
then

	cmd="${cmd} :jdroid-java:build :jdroid-java:test"
	cmd="${cmd} :jdroid-java-http-okhttp:build :jdroid-java-http-okhttp:test"
	cmd="${cmd} :jdroid-java-firebase-database:build :jdroid-java-firebase-database:test"

	if [ "$ENABLE_ANDROID" = "true" ]
	then
		cmd="${cmd} :jdroid-android:assemble :jdroid-android:testDebug :jdroid-android:lintDebug"
		cmd="${cmd} :jdroid-android-about:assemble :jdroid-android-about:testDebug :jdroid-android-about:lintDebug"
		cmd="${cmd} :jdroid-android-crashlytics:assemble :jdroid-android-crashlytics:testDebug :jdroid-android-crashlytics:lintDebug"
		cmd="${cmd} :jdroid-android-facebook:assemble :jdroid-android-facebook:testDebug :jdroid-android-facebook:lintDebug"
		cmd="${cmd} :jdroid-android-firebase-database:assemble :jdroid-android-firebase-database:testDebug :jdroid-android-firebase-database:lintDebug"
		cmd="${cmd} :jdroid-android-firebase-fcm:assemble :jdroid-android-firebase-fcm:testDebug :jdroid-android-firebase-fcm:lintDebug"
		cmd="${cmd} :jdroid-android-firebase-remoteconfig:assemble :jdroid-android-firebase-remoteconfig:testDebug :jdroid-android-firebase-remoteconfig:lintDebug"
		cmd="${cmd} :jdroid-android-google-admob:assemble :jdroid-android-google-admob:testDebug :jdroid-android-google-admob:lintDebug"
		cmd="${cmd} :jdroid-android-google-inappbilling:assemble :jdroid-android-google-inappbilling:testDebug :jdroid-android-google-inappbilling:lintDebug"
		cmd="${cmd} :jdroid-android-google-maps:assemble :jdroid-android-google-maps:testDebug :jdroid-android-google-maps:lintDebug"
		cmd="${cmd} :jdroid-android-google-plus:assemble :jdroid-android-google-plus:testDebug :jdroid-android-google-plus:lintDebug"
		cmd="${cmd} :jdroid-android-google-signin:assemble :jdroid-android-google-signin:testDebug :jdroid-android-google-signin:lintDebug"
		cmd="${cmd} :jdroid-android-twitter:assemble :jdroid-android-twitter:testDebug :jdroid-android-twitter:lintDebug"
		cmd="${cmd} :jdroid-android-sample:assembleUat :jdroid-android-sample:testDebug :jdroid-android-sample:lintDebug"
	fi

fi

# ************************
# Upload Snapshot to Maven repository
# ************************

if [ "$UPLOAD" = "true" ]
then

	cmd="${cmd} :jdroid-gradle-plugin:uploadArchives"
	cmd="${cmd} :jdroid-java:uploadArchives"
	cmd="${cmd} :jdroid-java-http-okhttp:uploadArchives"
	cmd="${cmd} :jdroid-java-firebase-database:uploadArchives"

	if [ "$ENABLE_ANDROID" = "true" ]
	then
		cmd="${cmd} :jdroid-android:uploadArchives"
		cmd="${cmd} :jdroid-android-about:uploadArchives"
		cmd="${cmd} :jdroid-android-crashlytics:uploadArchives"
		cmd="${cmd} :jdroid-android-facebook:uploadArchives"
		cmd="${cmd} :jdroid-android-firebase-database:uploadArchives"
		cmd="${cmd} :jdroid-android-firebase-fcm:uploadArchives"
		cmd="${cmd} :jdroid-android-firebase-remoteconfig:uploadArchives"
		cmd="${cmd} :jdroid-android-google-admob:uploadArchives"
		cmd="${cmd} :jdroid-android-google-inappbilling:uploadArchives"
		cmd="${cmd} :jdroid-android-google-maps:uploadArchives"
		cmd="${cmd} :jdroid-android-google-plus:uploadArchives"
		cmd="${cmd} :jdroid-android-google-signin:uploadArchives"
		cmd="${cmd} :jdroid-android-twitter:uploadArchives"
	fi
fi

cmd="${cmd} -PLOCAL_UPLOAD=$LOCAL_UPLOAD --configure-on-demand --refresh-dependencies --stacktrace"

if [ "$DEBUG" = "true" ]
then
	cmd="${cmd} --debug"
fi

echo "Executing the following command"
echo "${cmd}"
eval "${cmd}"
