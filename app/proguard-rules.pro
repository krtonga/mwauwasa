# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/mose/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

##---------------Begin: proguard configuration for Okhttp & Picasso  ----------

# FOR OKHTTP (official docs)
-dontwarn okhttp3.**
-dontwarn okhttp.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# FOR PICASSO (official docs)
-dontwarn com.squareup.okhttp.**
# FOR OKHTTP (workaround)
# The keep is excessive but does fix the compile error.
# TODO: figure out the internals of okhttp so as to not keep the whole
-keep class okhttp.** { *; }

##---------------End: proguard configuration for Okhttp & Picasso  ----------



##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class  com.github.codetanzania.core.model.** { *; }
-keep class  com.github.codetanzania.core.api.model.** { *; }
-keep class com.github.codetanzania.open311.android.library.api.models.** { *; }
-keep class com.github.codetanzania.open311.android.library.models.** { *; }
# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

##---------------End: proguard configuration for Gson  ----------



##---------------Begin: proguard configuration for Crashalytics  ----------
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
##---------------End: proguard configuration for Crashalytics  ----------
