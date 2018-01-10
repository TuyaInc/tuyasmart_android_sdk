# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Applications/Android Studio.app/sdk/tools/proguard/proguard-android.txt
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
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod
-repackageclasses ''
-allowaccessmodification
-printmapping map.txt

-optimizationpasses 7
-dontskipnonpubliclibraryclassmembers


# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
-dontpreverify
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,Annotation,EnclosingMethod,MethodParameters

-keep class **.R$* {
*;
}
# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

#java
-keep class java.awt.**{*;}
-dontwarn java.awt.**
-dontwarn junit.**
-dontwarn javax.microedition.khronos.opengles.GL10.**
-keep interface *
-keep class javax.jmdns.**{*;}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn javax.annotation.**

#android
-dontwarn android.support.**
-keep interface android.support.v7.**{*;}
-keep class android.support.**{*;}
-keep class * extends android.support.**{*;}
-keep class * extends android.app.*{*;}
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.os.IInterface
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.appwidget.AppWidgetProvider
-keep public class * extends android.webkit.*{*;}
-keep public class * extends android.widget.*{*;}
-keep public class * extends android.view.View{*;}
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep class * implements android.os.Parcelable{*;}
-keepclassmembers class * extends java.lang.Enum {
        public static **[] values();
        public static ** valueOf(java.lang.String);
        **[] $VALUES;
}
-keep class * implements java.io.Serializable{*;}
-keepclasseswithmembernames class * {
	public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
	public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep class org.json.**{*;}

#EventBus
-keep class de.greenrobot.event.**{*;}
-dontwarn de.greenrobot.event.**
-keepclassmembers class ** {
    public void onEvent*(**);
}

#commons
-keep class org.apache.commons.**{*;}
-dontwarn org.apache.commons.**

#fastJson
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.alibaba.fastjson.**

#netty
-keep class io.netty.** { *; }
-dontwarn io.netty.**

#volley
-keep class com.android.volley.** { *; }
-dontwarn com.android.volley.**

#mqtt
-keep class org.eclipse.paho.android.service.** { *; }
-keep class org.eclipse.paho.client.mqttv3.** { *; }

-dontwarn org.eclipse.paho.android.service.**
-dontwarn org.eclipse.paho.client.mqttv3.**

-dontwarn okio.**
-dontwarn rx.**
-keep class rx.**{ *; }

-dontwarn javax.annotation.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class okio.** { *; }
-dontwarn com.squareup.okhttp.**

#amap
-keep class com.amap.api.**
-keep class com.aps.**{*;}
-keep class com.amap.**{*;}
-keep class com.autonavi.**{*;}
-dontwarn com.autonavi.**
-dontwarn com.amap.**
-dontwarn com.aps.**
-dontwarn com.amap.api.**

#tuyasmart
-keep class * implements com.tuya.smart.android.base.mvp.bean.IBean{*;}
-keep class * implements com.tuya.smart.android.base.mvp.bean.IResponse{*;}
-keep class * implements com.tuya.smart.android.base.mvp.bean.IRequest{*;}
-keep class com.tuya.smart.android.base.mvp.bean.Result{*;}

-keep class com.tuya.smart.android.**{*;}
-dontwarn com.tuya.smart.android.**

-keep class u.aly.** {*;}
-dontwarn u.aly.**
-keep class javax.crypto.**{*;}
-dontwarn javax.crypto.**


-keep class com.taobao.securityjni.**{*;}
-keep class com.taobao.wireless.security.**{*;}
-keep class com.ut.secbody.**{*;}
-keep class com.taobao.dp.**{*;}
-keep class com.alibaba.wireless.security.**{*;}

-dontwarn android.**
-dontwarn org.apache.http.**
-dontwarn org.json.**

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,Annotation,EnclosingMethod,MethodParameters

-keep class **.R$* {
*;
}
-keep class com.tuya.smart.sdk.**{*;}
-dontwarn com.tuya.smart.sdk.**
