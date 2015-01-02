[![JDROID](https://raw.githubusercontent.com/maxirosson/jdroid/gh-pages/images/logoDark.png)](http://jdroidframework.com/)
[![Build Status](https://api.travis-ci.org/maxirosson/jdroid.svg?branch=master)](https://travis-ci.org/maxirosson/jdroid)
======

The JDROID Framework provides a programming and architectural model for modern Java-based enterprise and android applications.

Help us to continue with this project:

[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=2UEBTRTSCYA9L)

[<img src="https://raw.githubusercontent.com/maxirosson/jdroid/gh-pages/images/java.png" width="25" height="25"/> jdroid Java](https://github.com/maxirosson/jdroid/wiki/jdroid-Java)
-----------
Dependency project for both Android & Java apps
* HTTP Service Layer
 * [Apache HTTP Client implementation](https://hc.apache.org/)
 * GET, POST, PUT, PATCH & DELETE methods
 * Headers appender
 * Response validator
 * Response Mocks support
 * GZIP encoding
 * Cache support
* Parsers
 * [Jackson JSON parser](https://github.com/FasterXML/jackson)
 * Plain text parser
* JSON marshallers
* Mail Service
* Exception handling
* Utilities for Collections, Strings, Dates, Files, Encryption, Threads, [Logging](http://www.slf4j.org/), Validations, Reflection and more

[<img src="https://raw.githubusercontent.com/maxirosson/jdroid/gh-pages/images/android.png" width="25" height="25"/> jdroid Android](https://github.com/maxirosson/jdroid/wiki/jdroid-Android)
--------------
Library project for Android apps. Support for Android 4.0 (api level 14) and higher versions
* Languages supported: english & spanish
* [Navigation Drawer support](https://developer.android.com/design/patterns/navigation-drawer.html)
* Google Play Services integration
 * [Google Analytics v4 integration](https://developers.google.com/analytics/devguides/collection/android/v4/). A/B testing support
 * [Google Cloud Messaging integration](http://developer.android.com/google/gcm/index.html)
 * [Google Maps v2 integration](http://developer.android.com/google/play-services/maps.html)
 * [Google+ integration](http://developer.android.com/google/play-services/plus.html): +1 button, friends, sign in, sign out, revoke access, share
 * [Google Mobile Ads (AdMob) integration](http://developer.android.com/google/play-services/ads.html): banners & interstitials support
 * [Google Play In App Billing v3 integration](http://developer.android.com/google/play/billing/index.html)
* [Sqlite integration](https://sqlite.org/)
* [Android Universal Image Loader](https://github.com/nostra13/Android-Universal-Image-Loader)
* [Facebook integration](https://developers.facebook.com/docs/android/): sign in, sign out, share with deep link
* Exception handling
 * [Crashlytics integration](https://crashlytics.com/)
* [Merge adapter integration](https://github.com/commonsguy/cwac-merge)
* [Lint support](http://developer.android.com/tools/help/lint.html)
* [Strict mode support](http://developer.android.com/reference/android/os/StrictMode.html)
* Parsers
 * XML Pull Parser
* Debug settings support
* Url handling support
* House ads support
* Base Activity & Fragment implementations
* ListView & GridView pagination support
* Hero image and parallax effect support
* Loading & Refresh
 * Blocking loading dialog
 * Non blocking loading component
 * [Swipe to refresh support] (http://developer.android.com/reference/android/support/v4/widget/SwipeRefreshLayout.html)
 * Refresh action provider component
* Rate me component
* Picture import (From camera or gallery) component
* Barcode reading component
* Coverflow component
* Voice Recognizer component
* Date & Time picker components
* About dialog component
* QuickReturnLayout component
* Animations
 * Fade in / Fade out
* Utilities for Alarms, Bitmaps, [Notifications](http://developer.android.com/design/patterns/notifications.html), Shared Preferences, [Toasts](http://developer.android.com/guide/topics/ui/notifiers/toasts.html), GPS, and more

[<img src="https://raw.githubusercontent.com/maxirosson/jdroid/gh-pages/images/java.png" width="25" height="25"/> jdroid Java Web](https://github.com/maxirosson/jdroid/wiki/jdroid-Java-Web)
---------------
Dependency project for Java Web apps 
* Http Filters
 * Authentication filter
 * API version filter
* Parsers
 * Sax XML Parser
* [Spring MVC integration](http://projects.spring.io/spring-framework/)
* [Hibernate ORM integration](http://hibernate.org/orm/)
* [Restfb Facebook integration](http://restfb.com/)
* [Twitter 4j integration](http://twitter4j.org/)
* Generic push framework. [Google Cloud Messaging implementation](http://developer.android.com/google/gcm/index.html)
* Pagination and filtering support
* [Log4j logging support](http://logging.apache.org/log4j/1.2/)
* Utilities for Collections, CSV, Files, Reflection, [Guava](https://code.google.com/p/guava-libraries/) and more

[<img src="https://raw.githubusercontent.com/maxirosson/jdroid/gh-pages/images/android.png" width="25" height="25"/> jdroid Android Tools](https://github.com/maxirosson/jdroid/wiki/jdroid-Android-Tools)
--------------
Useful tools for Android apps developers
* Report the methods invocations in android dex files, package by package
* Publish APKs and listings (feature/promo graphics, High resolution icon, screenshots, title, short and full descriptions) on Google Play

[<img src="https://raw.githubusercontent.com/maxirosson/jdroid/gh-pages/images/genericNews.png" width="25" height="25"/> jdroid Scripts](https://github.com/maxirosson/jdroid/wiki/jdroid-Scripts)
--------------
A set of useful shell scripts to
 * Increment the pom & android manifest versions according to [Semantic Versioning](http://semver.org/)
 * Create a pull request on [Github](https://github.com)
 * Create a merge request on [Gitlab](https://www.gitlab.com/)
 * Count the methods invocations in android dex files
 * Strip Google Play Services jar
 * Start/stop and deploy on [Apache Tomcat](http://tomcat.apache.org/)
 * Automatically restart [Apache Tomcat](http://tomcat.apache.org/)

Apps using jdroid
--------------

|               | Google Play   |
| ------------- | ------------- |
| [<img src="https://raw.githubusercontent.com/maxirosson/jdroid/gh-pages/images/mediafever.png" width="40" height="40"/>](https://play.google.com/store/apps/details?id=com.mediafever&referrer=utm_source%3Djdroid) | Media Fever - Track your movies & series |
| [<img src="https://raw.githubusercontent.com/maxirosson/jdroid/gh-pages/images/subtefy.png" width="40" height="40"/>](https://play.google.com/store/apps/details?id=com.subtefy&referrer=utm_source%3Djdroid) | Subtefy - Alertas del Subte |
| [<img src="https://raw.githubusercontent.com/maxirosson/jdroid/gh-pages/images/codenumber.png" width="40" height="40"/>](https://play.google.com/store/apps/details?id=com.codenumber.lite) | Code Number - Bulls & Cows |

Acknowledgements
--------------

* [Android Arsenal](https://android-arsenal.com/details/1/1062)
* [Android Libs](http://android-libs.com/lib/jdroid)

--------------
For more information, visit the [GitHub Wiki](https://github.com/maxirosson/jdroid/wiki) or our [Site](http://jdroidframework.com/).

Follow us on [@jdroidframework](https://twitter.com/jdroidframework)
