jdroid
======

jdroid is an application framework for Android & Java apps. The project use [Semantic Versioning][3]

Help us to continue with this project:

[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=2UEBTRTSCYA9L)

jdroid Java
-----------
Dependency project for both Android & Java apps
* HTTP Service Layer
 * Apache HTTP Client implementation
 * GET, POST, PUT & DELETE methods
 * Headers appender
 * Response validator
 * Response Mocks support
 * GZIP encoding
 * Cache support
* JSON & Plain text parsers
* JSON marshallers
* Mail Service
* Exception handling
* Utilities for Collections, Strings, Dates, Files, Encryption, Threads, Logging, Validations, Reflection and more

jdroid Java Web
---------------
Dependency project for Java Web apps 
* [Spring MVC integration][7]
* [Hibernate ORM integration][6]
* [Restfb Facebook integration][19]
* Generic push framework. [Google Cloud Messaging implementation][8]
* Pagination and filtering support
* [Log4j logging support][17]
* Utilities for Collections, CSV, Files, Reflection, [Guava][16] and more

jdroid Android
--------------
Library project for Android apps. Support for Android 4.0 (api level 14) and higher versions
* [Navigation Drawer support][15]
* Google Analytics integration
* Flurry Analytics integration
* Google Play Services integration
 * [Google Cloud Messaging integration][8]
 * [Google Maps v2 integration][9]
 * [Google+ integration][11]: +1 button, friends, sign in, sign out, revoke access, share
 * [AdMob integration][10]
* [Sqlite integration][12]
* [Android Universal Image Loader][4]
* [In App Billing integration][13]
* [Facebook integration][14]: sign in, sign out, share with deep link
* [Twitter integration][18]
* [Crittercism integration][5]
* Lint support
* Strict mode support
* Debug settings support
* Url handling support
* Base Activity & Fragment implementations
* Exception handling
* ListView & GridView pagination support
* Picture import (From camera or gallery) component
* Barcode reading component
* Refresh action provider component
* Coverflow component
* Voice Recognizer component
* Date & Time picker components
* About dialog component
* Animations
 * Fade in / Fade out
* Utilities for Alarms, Bitmaps, Notifications, Shared Preferences, Toasts, Sounds, GPS, and more

jdroid scripts
--------------
A set of useful shell scripts to
 * Increment the pom & android manifest versions according to [Semantic Versioning][3]
 * Create a pull request on [Github][22]
 * Create a merge request on [Gitlab][21]
 * Count the methods in android dex files
 * Start/stop and deploy on [Apache Tomcat][20]
 * Automatically restart [Apache Tomcat][20]

jdroid sample server
--------------
Sample server app using jdroid Java Web & jdroid Java

Apps using jdroid
--------------

<a href="https://play.google.com/store/apps/details?id=com.mediafever">
  <img alt="Get it on Google Play"
       src="https://github.com/maxirosson/media-fever/blob/gh-pages/images/featureGraphic.png?raw=true" />
</a>

<a href="https://play.google.com/store/apps/details?id=com.codenumber.lite">
  <img alt="Get it on Google Play"
       src="https://github.com/maxirosson/code-number/blob/master/codenumber.png?raw=true" />
</a>

--------------
For more information, visit the [GitHub Wiki][1] or our [Site][2].

[1]: https://github.com/maxirosson/jdroid/wiki
[2]: http://maxirosson.github.com/jdroid/
[3]: http://semver.org/
[4]: https://github.com/nostra13/Android-Universal-Image-Loader
[5]: https://www.crittercism.com/
[6]: http://hibernate.org/orm/
[7]: http://projects.spring.io/spring-framework/
[8]: http://developer.android.com/google/gcm/index.html
[9]: http://developer.android.com/google/play-services/maps.html
[10]: http://developer.android.com/google/play-services/ads.html
[11]: http://developer.android.com/google/play-services/plus.html
[12]: https://sqlite.org/
[13]: http://developer.android.com/google/play/billing/index.html
[14]: https://developers.facebook.com/docs/android/
[15]: https://developer.android.com/design/patterns/navigation-drawer.html
[16]: https://code.google.com/p/guava-libraries/
[17]: http://logging.apache.org/log4j/1.2/
[18]: http://twitter4j.org/
[19]: http://restfb.com/
[20]: http://tomcat.apache.org/
[21]: https://www.gitlab.com/
[22]: https://github.com
