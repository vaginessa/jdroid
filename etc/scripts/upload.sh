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
./gradlew :jdroid-java:uploadArchives --configure-on-demand -PLOCAL_UPLOAD=$LOCAL_UPLOAD --refresh-dependencies

cmd="./gradlew "

cmd="${cmd} :jdroid-java-http-okhttp:uploadArchives"
cmd="${cmd} :jdroid-java-firebase:uploadArchives"

cmd="${cmd} :jdroid-javaweb:uploadArchives"

cmd="${cmd} :jdroid-android:uploadArchives"
cmd="${cmd} :jdroid-android-about:uploadArchives"
cmd="${cmd} :jdroid-android-crashlytics:uploadArchives"
cmd="${cmd} :jdroid-android-facebook:uploadArchives"
cmd="${cmd} :jdroid-android-google-admob:uploadArchives"
cmd="${cmd} :jdroid-android-google-gcm:uploadArchives"
cmd="${cmd} :jdroid-android-google-maps:uploadArchives"
cmd="${cmd} :jdroid-android-google-plus:uploadArchives"
cmd="${cmd} :jdroid-android-about:uploadArchives"
cmd="${cmd} -PLOCAL_UPLOAD=$LOCAL_UPLOAD --refresh-dependencies"

echo "Executing the following command"
echo "${cmd}"
eval "${cmd}"