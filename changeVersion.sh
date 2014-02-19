#!/bin/sh

SOURCE_DIRECTORY=$1
VERSION=$2

# Change version
# ************************

sh $SOURCE_DIRECTORY/jdroid-scripts/replaceXmlTag.sh $SOURCE_DIRECTORY/pom.xml 'version' $VERSION
sh $SOURCE_DIRECTORY/jdroid-scripts/replaceXmlTag.sh $SOURCE_DIRECTORY/jdroid-java/pom.xml 'version' $VERSION
sh $SOURCE_DIRECTORY/jdroid-scripts/replaceXmlTag.sh $SOURCE_DIRECTORY/jdroid-android/pom.xml 'version' $VERSION
sh $SOURCE_DIRECTORY/jdroid-scripts/replaceXmlTag.sh $SOURCE_DIRECTORY/jdroid-javaweb/pom.xml 'version' $VERSION
sh $SOURCE_DIRECTORY/jdroid-scripts/replaceXmlTag.sh $SOURCE_DIRECTORY/jdroid-sample-server/pom.xml 'version' $VERSION

git diff HEAD
git add -A
git commit -m "Changed jdroid version to v$VERSION"