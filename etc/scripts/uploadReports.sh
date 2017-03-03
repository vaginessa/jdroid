#!/bin/sh

if [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
  echo -e "Starting to update gh-pages\n"

  # copy data we're interested in to other place
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-about/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-crashlytics/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-facebook/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-database/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-crash/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-fcm/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-remoteconfig/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-admob/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-inappbilling/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-maps/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-plus/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-signin/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-twitter/build/outputs
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-sample/build/outputs

  cp jdroid-android/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android/build/outputs/lint-results.html
  cp jdroid-android-about/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-about/build/outputs/lint-results.html
  cp jdroid-android-crashlytics/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-crashlytics/build/outputs/lint-results.html
  cp jdroid-android-facebook/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-facebook/build/outputs/lint-results.html
  cp jdroid-android-firebase-crash/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-crash/build/outputs/lint-results.html
  cp jdroid-android-firebase-database/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-database/build/outputs/lint-results.html
  cp jdroid-android-firebase-fcm/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-fcm/build/outputs/lint-results.html
  cp jdroid-android-firebase-remoteconfig/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-remoteconfig/build/outputs/lint-results.html
  cp jdroid-android-firebase-admob/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-admob/build/outputs/lint-results.html
  cp jdroid-android-google-inappbilling/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-inappbilling/build/outputs/lint-results.html
  cp jdroid-android-google-maps/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-maps/build/outputs/lint-results.html
  cp jdroid-android-google-plus/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-plus/build/outputs/lint-results.html
  cp jdroid-android-google-signin/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-signin/build/outputs/lint-results.html
  cp jdroid-android-twitter/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-twitter/build/outputs/lint-results.html
  cp jdroid-android-sample/build/outputs/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-sample/build/outputs/lint-results.html

  # go to home and setup git
  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "Travis"

  # using token clone gh-pages branch
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/maxirosson/jdroid.git  gh-pages > /dev/null

  # go into diractory and copy data we're interested in to that directory
  cd gh-pages
  mkdir -p ./reports/$TRAVIS_BRANCH
  cp -Rf $HOME/reports/$TRAVIS_BRANCH/* ./reports/$TRAVIS_BRANCH

  # add, commit and push files
  git add -f .
  git commit -m "Travis build $TRAVIS_BUILD_NUMBER pushed to gh-pages"
  git push -fq origin gh-pages > /dev/null

fi