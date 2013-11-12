#!/bin/sh

OAUTH_TOKEN=$1
REPO_OWNER=$2
PROJECT_NAME=$3
TARGET_BRANCH=$4

if [ -z "$TARGET_BRANCH" ]
then
	TARGET_BRANCH="master"
fi

TITLE=$(git log -1 --pretty=%B)
CURRENT_BRANCH=$(git symbolic-ref HEAD | sed -e 's,.*/\(.*\),\1,')

curl \
    -X POST \
    -H "Authorization: token $OAUTH_TOKEN" \
    -d@- \
    "https://api.github.com/repos/$REPO_OWNER/$PROJECT_NAME/pulls" <<EOF
{
  "title": "$TITLE",
  "head": "$CURRENT_BRANCH",
  "base": "$TARGET_BRANCH"
}
EOF
