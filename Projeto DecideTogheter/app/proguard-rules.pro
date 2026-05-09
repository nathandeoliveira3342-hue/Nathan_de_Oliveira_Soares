# Regras ProGuard para o DecideTogether
# Adicionadas para compatibilidade com o SDK do Jitsi Meet

# Mantém as classes do Jitsi Meet SDK
-keep class org.jitsi.meet.** { *; }
-keep class org.webrtc.** { *; }

# Mantém os modelos de dados do app (evita problemas com Gson/serialização)
-keep class com.decidetogether.model.** { *; }

# Mantém as Activities e Fragments
-keep class com.decidetogether.ui.** { *; }

# Regras padrão do Android
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
