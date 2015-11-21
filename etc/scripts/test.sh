#!/bin/sh

# Note the set -ev at the top. The -e flag causes the script to exit as soon as one command
# returns a non-zero exit code. The -v flag makes the shell print all lines in the script
# before executing them, which helps identify which steps failed.
set -ev

UPLOAD=$1
ENABLE_JAVA_WEB=$2
ENABLE_ANDROID=$3

if [ -z "$ENABLE_JAVA_WEB" ]
then
	ENABLE_JAVA_WEB="true"
fi

if [ -z "$ENABLE_ANDROID" ]
then
	ENABLE_ANDROID="true"
fi

if [ -z "$UPLOAD" ]
then
	UPLOAD="false"
fi

# ************************
# jdroid gradle plugin
# ************************

./gradlew :jdroid-java:clean :jdroid-java:uploadArchives :jdroid-gradle-plugin:clean :jdroid-gradle-plugin:uploadArchives --configure-on-demand -PLOCAL_UPLOAD=true

# ************************
# jdroid javaweb sample
# ************************

if [ "$ENABLE_JAVA_WEB" = "true" ]
then
	./gradlew :jdroid-java:clean :jdroid-java:build :jdroid-java:test :jdroid-java-http-okhttp:build :jdroid-java-http-okhttp:test :jdroid-javaweb:clean :jdroid-javaweb:build :jdroid-javaweb:test :jdroid-javaweb-sample:build --configure-on-demand
fi

# ************************
# jdroid android sample
# ************************

if [ "$ENABLE_ANDROID" = "true" ]
then
	./gradlew :jdroid-android:clean :jdroid-android:build :jdroid-android:testDebug :jdroid-android-crashlytics:clean :jdroid-android-crashlytics:build :jdroid-android-facebook:clean :jdroid-android-facebook:build :jdroid-android-facebook:testDebug :jdroid-android-google-admob:clean :jdroid-android-google-admob:build :jdroid-android-google-maps:clean :jdroid-android-google-maps:build :jdroid-android-google-maps:testDebug :jdroid-android-google-gcm:clean :jdroid-android-google-gcm:build :jdroid-android-google-gcm:testDebug :jdroid-android-google-plus:clean :jdroid-android-google-plus:build :jdroid-android-google-plus:testDebug :jdroid-android-about:clean :jdroid-android-about:build :jdroid-android-about:testDebug :jdroid-android-sample:check :jdroid-android-sample:assembleUat :jdroid-android-sample:countMethodsSummary
fi

# ************************
# Upload Snapshot to Maven repository
# ************************

if [ "$UPLOAD" = "true" ]
then

	if [ "$ENABLE_JAVA_WEB" = "true" ]
	then
		./gradlew :jdroid-gradle-plugin:uploadArchives :jdroid-java:uploadArchives :jdroid-java-http-okhttp:uploadArchives :jdroid-javaweb:uploadArchives --configure-on-demand
	fi

	if [ "$ENABLE_ANDROID" = "true" ]
	then
		./gradlew :jdroid-android:uploadArchives :jdroid-android-about:uploadArchives :jdroid-android-crashlytics:uploadArchives :jdroid-android-facebook:uploadArchives :jdroid-android-google-maps:uploadArchives :jdroid-android-google-admob:uploadArchives :jdroid-android-google-gcm:uploadArchives :jdroid-android-google-maps:uploadArchives :jdroid-android-google-plus:uploadArchives
	fi
fi
