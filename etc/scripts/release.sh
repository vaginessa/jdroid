#!/bin/sh
set -e

BUILD_DIRECTORY=$1
OAUTH_TOKEN=$2

PROJECT_NAME=jdroid
SOURCE_DIRECTORY=$BUILD_DIRECTORY/$PROJECT_NAME/source/$PROJECT_NAME

# Build and Deploy to Sonatype
# ************************
sh $JDROID_HOME/etc/scripts/build.sh $BUILD_DIRECTORY false production false true

# Upload Release on GitHub
# ************************

REPO_OWNER=maxirosson
BODY=`cat ./etc/releaseNotes.txt`
TAG_NAME=v`./gradlew :printVersion -q --configure-on-demand`

RESPONSE=$(curl \
    -X POST \
    -H "Authorization: token $OAUTH_TOKEN" \
    -d@- \
    "https://api.github.com/repos/$REPO_OWNER/$PROJECT_NAME/releases" <<EOF
{
  "tag_name": "$TAG_NAME",
  "target_commitish": "master",
  "name": "$TAG_NAME",
  "draft": false,
  "prerelease": false,
  "body": "$BODY"
}
EOF)

