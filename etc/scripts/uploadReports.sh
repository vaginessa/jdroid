#!/bin/sh

if [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
  echo -e "Starting to update jdroid-website\n"

  # copy data we're interested in to other place
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-about/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-facebook/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-instanceid/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-database/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-crashlytics/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-fcm/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-remoteconfig/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-admob/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-inappbilling/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-maps/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-plus/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-signin/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-sample/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-twitter/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-uil/build/reports

  cp -R jdroid-android/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android/build/reports/
  cp -R jdroid-android-about/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-about/build/reports/
  cp -R jdroid-android-facebook/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-facebook/build/reports/
  cp -R jdroid-android-firebase-instanceid/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-instanceid/build/reports/
  cp -R jdroid-android-firebase-crashlytics/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-crashlytics/build/reports/
  cp -R jdroid-android-firebase-database/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-database/build/reports/
  cp -R jdroid-android-firebase-fcm/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-fcm/build/reports/
  cp -R jdroid-android-firebase-remoteconfig/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-remoteconfig/build/reports/
  cp -R jdroid-android-firebase-admob/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-admob/build/reports/
  cp -R jdroid-android-google-inappbilling/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-inappbilling/build/reports/
  cp -R jdroid-android-google-maps/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-maps/build/reports/
  cp -R jdroid-android-google-plus/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-plus/build/reports/
  cp -R jdroid-android-google-signin/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-signin/build/reports/
  cp -R jdroid-android-twitter/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-twitter/build/reports/
  cp -R jdroid-android-sample/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-sample/build/reports/
  cp -R jdroid-android-uil/build/reports/* $HOME/reports/$TRAVIS_BRANCH/jdroid-android-uil/build/reports/

  # go to home and setup git
  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "Travis"

  # using token clone master branch
  git clone --quiet https://${GH_TOKEN}@github.com/maxirosson/jdroid-website.git > /dev/null

  # go into directory and copy data we're interested in to that directory
  cd jdroid-website
  mkdir -p ./reports/$TRAVIS_BRANCH
  cp -Rf $HOME/reports/$TRAVIS_BRANCH/* ./reports/$TRAVIS_BRANCH

  # add, commit and push files
  git add -f .
  git commit -m "Travis build $TRAVIS_BUILD_NUMBER pushed to master"
  git push -fq origin master > /dev/null

fi