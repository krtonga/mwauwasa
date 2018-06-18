# MAJIFIX ANDROID APP

> Note: Main documentation is found in the wiki at: https://github.com/CodeTanzania/open311-android/wiki

## About
This repository contains android source code for the MajiFix app. This app is in production in Dar es Salaam, Tanzania, as the [DAWASCO app](https://play.google.com/store/apps/details?id=com.customerinfo). 

## Requirements And Dependencies
The minimum target is Android SDK Level 17 (Devices supporting Android 4.2 and above)

Being a fork of an open source, the application utilizes open source technologies to implement the features. Table below depicts features and their reliance on open source technologies.

### Featured libraries
| Library | Feature |
|---|---|
| The majifix library | A collection of features used to report civic related problems. |
| libphonenumber | Verify User's phone number |
| crashnalytics/ hockeyapp | Bring Mobile DevOps to the app with beta distribution, crash reporting, user metrics, feedback, and powerful workflow integrations. |
| mapbox | android library for the Open Street Maps |

> **Note:** The app also includes support libraries which are necessary during the development of an android app in the aforementioned minimum target.

#### Test libraries
| Library | Usage |
|---------|-------|
| espresso | Write concise, beautiful, and reliable Android UI tests |
| JUnit | Write Unit Test in Java Platforms. |
| Roboelectric | Robolectric is a unit test framework that de-fangs the Android SDK jar so you can test-drive the development of your Android app.|