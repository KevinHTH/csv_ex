# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/legolas/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-keep class io.reactivex.**{*;}
-keep interface io.reactivex.**{*;}
-keep class com.jakewharton.**{*;}
-keep interface com.jakewharton.**{*;}
-keep class com.squareup.**{*;}
-keep interface com.squareup.**{*;}
-keep class com.facebook.rebound.**{*;}
-keep interface com.facebook.rebound.**{*;}
-keep class com.github.viethoa.**{*;}
-keep interface com.github.viethoa.**{*;}
-keep class com.crashlytics.**{*;}
-keep interface com.crashlytics.**{*;}
-keep class io.paperdb.**{*;}
-keep interface io.paperdb.**{*;}
-keep class com.hkm.ezwebview.**{*;}
-keep interface com.hkm.ezwebview.**{*;}
-keep class com.afollestad.**{*;}
-keep interface com.afollestad.**{*;}
-keep class org.projectlombok.**{*;}
-keep interface org.projectlombok.**{*;}
-keeppackagenames org.jsoup.nodes

-keep class org.w3c.dom.**{*;}
-keep interface org.w3c.dom.**{*;}
-keep class com.roughike.**{*;}
-keep interface com.roughike.**{*;}

-dontwarn com.roughike.**
-dontwarn butterknife.internal.**
-dontwarn okio.**
-dontwarn rx.internal.**
-dontwarn org.w3c.dom.**
# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.

#Source

#Retrofit
-keep class retrofit2.** { *; }
-keep interface retrofit2.**{*;}
-keep class retrofit.** { *; }
-keep interface retrofit.**{*;}
-dontwarn retrofit.**
-dontwarn retrofit2.**

#GCM
-keep class com.google.android.gcm.**{*;}
-keep interface com.google.android.gcm.**{*;}

#dagger
-keep class com.google.dagger.**{*;}
-keep interface com.google.dagger.**{*;}
-dontwarn com.google.dagger.**
#piccaso
-keep class com.squareup.picasso.**{*;}
-keep interface com.squareup.picasso.**{*;}
-keepclassmembers class com.squareup.picasso.**{*;}
-dontwarn com.squareup.picasso.**
-keepclasseswithmembers class * {
    @com.squareup.picasso.** *;
}
-keepclassmembers class * {
    @com.squareup.picasso.** *;
}
-dontwarn com.squareup.picasso.**

#retrolamda
-dontwarn java.lang.invoke.*
#ok http
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** {*; }
-keep interface okhttp3.** {*; }
-dontwarn okhttp3.**

#com.squareup
-keep class com.squareup.okhttp.**{*;}
-keep interface com.squareup.okhttp.**{*;}
-dontwarn com.squareup.okhttp.**

#com.github.afollestad.material-dialogs
-keep class com.github.afollestad.material-dialogs.**{*;}
-keep interface com.github.afollestad.material-dialogs.**{*;}
-dontwarn com.github.afollestad.material-dialogs.**

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
   @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
   @butterknife.* <methods>;
}
#rxjava
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
  long producerIndex;
  long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
   rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#com.google.code.gson
-keep class com.google.code.gson.**{*;}
-keep interface com.google.code.gson.**{*;}
-dontwarn com.google.code.gson.**

#fabric
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

