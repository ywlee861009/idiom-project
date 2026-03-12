# Realm Kotlin specific rules
-keep class io.realm.kotlin.** { *; }
-keep class * implements io.realm.kotlin.types.RealmObject { *; }
-keep class * implements io.realm.kotlin.types.EmbeddedRealmObject { *; }

# Kotlin Serialization
-keepattributes *Annotation*, EnclosingMethod, Signature
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable *;
}

# Compose specific rules
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.material3.** { *; }

# AdMob
-keep class com.google.android.gms.ads.** { *; }

# Firebase
-keep class com.google.firebase.** { *; }
