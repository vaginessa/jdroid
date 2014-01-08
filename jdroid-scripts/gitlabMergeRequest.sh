#!/bin/sh

PRIVATE_TOKEN=$1
PROJECT_ID=$2
TARGET_BRANCH=$3

# Help
# ****
if [ $# -eq 1 ] && [ $1 = -h ]
then
        echo "Help"
        echo "****"
        echo ""
        echo "This script will create a merge request on Gitlab."
        echo "Available parameters"
        echo ""
        echo " 1) The private token used to be authenticated on the api. This parameter is required. You can obtain it from http://gitlab.com/profile/account."
        echo ""
        echo " 2) The id of the project. This parameter is required. You can obtain it from http://gitlab.com/api/v3/projects?private_token=?????????"
        echo ""
        echo " 3) The target branch of the merge request. This parameter is optional and the default value is 'master'"
        echo ""
        exit 0
fi

if [ -z "$TARGET_BRANCH" ]
then
	TARGET_BRANCH="master"
fi

TITLE=$(git log -1 --pretty=%B)
CURRENT_BRANCH=$(git symbolic-ref HEAD | sed -e 's,.*/\(.*\),\1,')

curl \
    -X POST \
    -H "PRIVATE-TOKEN: $PRIVATE_TOKEN" \
    -H "Content-Type: application/json" \
    -d@- \
    "https://gitlab.com/api/v3/projects/$PROJECT_ID/merge_requests" <<EOF
{
  "title": "$TITLE",
  "source_branch": "$CURRENT_BRANCH",
  "target_branch": "$TARGET_BRANCH"
}
EOF
echo