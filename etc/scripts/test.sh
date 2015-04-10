#!/bin/sh

# Note the set -ev at the top. The -e flag causes the script to exit as soon as one command
# returns a non-zero exit code. The -v flag makes the shell print all lines in the script
# before executing them, which helps identify which steps failed.
set -ev

UPLOAD=$1

if [ -z "$UPLOAD" ]
then
	UPLOAD="false"
fi

# ************************
# jdroid gradle plugin
# ************************

./gradlew :jdroid-java:clean :jdroid-java:uploadArchives :jdroid-gradle-plugin:clean :jdroid-gradle-plugin:uploadArchives --configure-on-demand -PLOCAL_UPLOAD=true

# ************************
# jdroid
# ************************

./gradlew :jdroid-java:clean :jdroid-java:build :jdroid-android:clean :jdroid-android:build :jdroid-javaweb:clean :jdroid-javaweb:build

# ************************
# jdroid sample android
# ************************

./gradlew :jdroid-sample-android:check :jdroid-sample-android:assembleUat :jdroid-sample-android:countMethodsSummary

# ************************
# jdroid sample server
# ************************

./gradlew :jdroid-sample-server:build

# ************************
# Deploy Snapshot to Maven repository
# ************************

if [ "$UPLOAD" = "true" ]
then
	./gradlew :jdroid-gradle-plugin:uploadArchives :jdroid-java:uploadArchives :jdroid-javaweb:uploadArchives :jdroid-android:uploadArchives
fi
