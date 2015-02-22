#!/bin/sh

# Note the set -ev at the top. The -e flag causes the script to exit as soon as one command
# returns a non-zero exit code. The -v flag makes the shell print all lines in the script
# before executing them, which helps identify which steps failed.
set -e


# ************************
# jdroid
# ************************

./gradlew build

# ************************
# jdroid sample android
# ************************

cd jdroid-sample-android
../gradlew build

# ************************
# jdroid sample server
# ************************

cd ../jdroid-sample-server
../gradlew build

