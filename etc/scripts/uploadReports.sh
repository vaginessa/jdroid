#!/bin/sh

if [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
  echo -e "Starting to update gh-pages\n"

  # copy data we're interested in to other place
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-about/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-facebook/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-database/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-crash/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-fcm/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-remoteconfig/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-admob/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-inappbilling/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-maps/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-plus/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-signin/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-fabric/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-twitter/build/reports
  mkdir -p $HOME/reports/$TRAVIS_BRANCH/jdroid-android-sample/build/reports

  cp jdroid-android/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android/build/reports/lint-results.html
  cp jdroid-android-about/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-about/build/reports/lint-results.html
  cp jdroid-android-facebook/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-facebook/build/reports/lint-results.html
  cp jdroid-android-firebase-crash/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-crash/build/reports/lint-results.html
  cp jdroid-android-firebase-database/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-database/build/reports/lint-results.html
  cp jdroid-android-firebase-fcm/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-fcm/build/reports/lint-results.html
  cp jdroid-android-firebase-remoteconfig/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-remoteconfig/build/reports/lint-results.html
  cp jdroid-android-firebase-admob/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-firebase-admob/build/reports/lint-results.html
  cp jdroid-android-google-inappbilling/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-inappbilling/build/reports/lint-results.html
  cp jdroid-android-google-maps/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-maps/build/reports/lint-results.html
  cp jdroid-android-google-plus/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-plus/build/reports/lint-results.html
  cp jdroid-android-google-signin/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-google-signin/build/reports/lint-results.html
  cp jdroid-android-fabric/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-fabric/build/reports/lint-results.html
  cp jdroid-android-twitter/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-twitter/build/reports/lint-results.html
  cp jdroid-android-sample/build/reports/lint-results-debug.html $HOME/reports/$TRAVIS_BRANCH/jdroid-android-sample/build/reports/lint-results.html

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