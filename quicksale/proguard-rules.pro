# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
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
#keep住源文件及行号
-keepattributes SourceFile,LineNumberTable,EnclosingMethod
 #不需要混淆
 #activity
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-dontwarn android.support.v4.**
-dontwarn android.annotation
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep class android.support.v4.**{*;}
-keepclasseswithmembernames class * {
    native <methods>; 
}
 -keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
 -keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
 -keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
 #serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep public class * implements java.io.Serializable {*;}
#model
-keep class com.nahuo.wp.model.** { *; }
-keep class com.nahuo.wp.orderdetail.model.** { *; }
#baidu
-keep class com.baidu.** { *; } 
-keep class vi.com.gdi.bgl.android.**{*;}
#eventbus
-keepclassmembers class ** {
    public void onEvent*(**);
}
#jpush
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
#SlidingMenu
-dontwarn com.jeremyfeinstein.slidingmenu.lib.**
-keep class com.jeremyfeinstein.slidingmenu.lib.**{*;}
#Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
#enum
 -keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#aidl
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#编译无视警告
-dontskipnonpubliclibraryclasses
-dontwarn com.squareup.**
-dontwarn com.slidingmenu.**
-dontwarn com.facebook.**
-dontwarn com.nahuo.**
-dontwarn org.slf4j.**
-dontwarn ch.imvs.sdes4j.**

#tencent
-keep class com.tencent.mm.**{*;}
-keep class com.tencent.** { *; } 
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
-keep public class com.tencent.** {*;}
-keep public interface com.tencent.**

#sina
-keep class com.sina.weibo.sdk.** { *; }

#环信
-keep class com.easemob.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}
-dontwarn  com.easemob.**
-keep class com.nahuo.wp.common.SmileUtils {*;}
-dontwarn ch.imvs.**
-dontwarn org.slf4j.**
-keep class org.ice4j.** {*;}
-keep class net.java.sip.** {*;}
-keep class org.webrtc.voiceengine.** {*;}
-keep class org.bitlet.** {*;}
-keep class org.slf4j.** {*;}
-keep class ch.imvs.** {*;}