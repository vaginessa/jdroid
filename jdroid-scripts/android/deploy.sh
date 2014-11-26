#!/bin/sh

PROJECT_BUILD_DIRECTORY=$1
APKS_DIRECTORY=$2

ASSEMBLIES_DIRECTORY=$PROJECT_BUILD_DIRECTORY/assemblies

# Copy the apks to dropbox
find $ASSEMBLIES_DIRECTORY -type f -name '*.apk' -exec mv {} $APKS_DIRECTORY \;