#!/bin/sh

# Note the set -ev at the top. The -e flag causes the script to exit as soon as one command
# returns a non-zero exit code. The -v flag makes the shell print all lines in the script
# before executing them, which helps identify which steps failed.
set -e

UPLOAD=$1

if [ -z "$UPLOAD" ]
then
	UPLOAD="false"
fi

# ************************
# jdroid gradle plugin
# ************************

./gradlew clean :jdroid-gradle-plugin:build :jdroid-gradle-plugin:uploadArchives --refresh-dependencies -PLOCAL_UPLOAD=true

# ************************
# jdroid
# ************************

./gradlew :jdroid-java:build :jdroid-android:build :jdroid-javaweb:build --refresh-dependencies

# ************************
# jdroid sample android
# ************************

cd jdroid-sample-android
../gradlew check assembleUat countMethodsSummary

# ************************
# jdroid sample server
# ************************

cd ../jdroid-sample-server
../gradlew build

# ************************
# Deploy Snapshot to Maven repository
# ************************

cd ..
if [ "$UPLOAD" = "true" ]
then
	./gradlew :jdroid-gradle-plugin:uploadArchives :jdroid-java:uploadArchives :jdroid-javaweb:uploadArchives :jdroid-android:uploadArchives
fi
