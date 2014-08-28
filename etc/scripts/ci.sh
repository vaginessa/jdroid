#!/bin/sh

# Note the set -ev at the top. The -e flag causes the script to exit as soon as one command 
# returns a non-zero exit code. The -v flag makes the shell print all lines in the script 
# before executing them, which helps identify which steps failed.
set -ev

# ************************
# Tests Execution
# ************************

mvn clean install -P jdroid-test

# ************************
# Deploy Snapshot
# ************************
mvn clean deploy -P jdroid-release -Dmaven.test.skip=true --settings ./settings.xml

# ************************
# jdroid sample android
# ************************

cd jdroid-sample-android
mvn clean test -P jdroid-test
mvn clean install -P jdroid-sample-android-uat -Dmaven.test.skip=true

# ************************
# jdroid sample server
# ************************

cd ../jdroid-sample-server
mvn clean test -P jdroid-test
mvn clean assembly:assembly -P jdroid-sample-server-uat -Dmaven.test.skip=true
