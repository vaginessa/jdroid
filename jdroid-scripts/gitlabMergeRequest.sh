#!/bin/sh

OAUTH_TOKEN=$1
REPO_ID=$2
TARGET_BRANCH=$3

if [ -z "$TARGET_BRANCH" ]
then
	TARGET_BRANCH="master"
fi

TITLE=$(git log -1 --pretty=%B)
CURRENT_BRANCH=$(git symbolic-ref HEAD | sed -e 's,.*/\(.*\),\1,')

curl \
    -X POST \
    -H "PRIVATE-TOKEN: $OAUTH_TOKEN" \
    -H "Content-Type: application/json" \
    -d@- \
    "https://gitlab.com/api/v3/projects/$REPO_ID/merge_requests" <<EOF
{
  "title": "$TITLE",
  "source_branch": "$CURRENT_BRANCH",
  "target_branch": "$TARGET_BRANCH"
}
EOF
echo