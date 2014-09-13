#!/bin/sh

JDROID_HOME=$1
PROJECT_PATH=$2
VERSION_TYPE=$3

if [ -z "$VERSION_TYPE" ]
then
	echo "[ERROR] The VERSION_TYPE parameter is required"
	exit 1;
fi

if [ -z "$COMMIT" ]
then
	COMMIT="true"
fi

# Change Android Manifest version

MANIFEST_PATH=$PROJECT_PATH/AndroidManifest.xml

currentVersionName=`grep -m 1 "android:versionName=" $MANIFEST_PATH | sed 's/.*android:versionName=\"//g' | sed 's/\" .*//g'`
echo "The current version name is $currentVersionName"

currentVersionCode=`grep -m 1 "android:versionCode=" $MANIFEST_PATH | sed 's/.*android:versionCode=\"//g' | sed 's/\" .*//g'`
echo "The current version code is $currentVersionCode"

OIFS=$IFS
set -- "$currentVersionName" 
IFS="."
versionItems=($*)
IFS=$OIFS

major="${versionItems[0]}"
minor="${versionItems[1]}" 
patch="${versionItems[2]}" 

VERSION_CODE=$currentVersionCode

if [ "$VERSION_TYPE" = "major" ]
then
    VERSION_CODE=$((VERSION_CODE+10000))
    VERSION_CODE=$((VERSION_CODE-($minor*100)))
    VERSION_CODE=$((VERSION_CODE-$patch))
	major=$((major+1))
	minor='0'
	patch='0'
fi

if [ "$VERSION_TYPE" = "minor" ]
then
    VERSION_CODE=$((VERSION_CODE+100))
    VERSION_CODE=$((VERSION_CODE-$patch))
	minor=$((minor+1))
	patch='0'
fi

if [ "$VERSION_TYPE" = "patch" ]
then
	VERSION_CODE=$((VERSION_CODE+1))
	patch=$((patch+1))
fi

VERSION_NAME=$major.$minor.$patch
echo "The new version name is $VERSION_NAME"

echo "The new version code is $VERSION_CODE"


# Creating a temporary file for sed to write the changes to
temp_file="repl.temp"
 
sed -e "s/android:versionCode=\"$currentVersionCode\"/android:versionCode=\"$VERSION_CODE\"/g" $MANIFEST_PATH | sed -e "s/android:versionName=\"$currentVersionName\"/android:versionName=\"$VERSION_NAME\"/g" > $temp_file
 
# Writing our changes back to the original file
chmod 666 $MANIFEST_PATH
mv $temp_file $MANIFEST_PATH
