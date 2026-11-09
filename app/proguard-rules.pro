# Room：保留实体与 DAO，以便数据库字段名与迁移兼容
-keep class com.fangshen.manual.data.entity.** { *; }
-keep class com.fangshen.manual.data.** { *; }

# Compose / Vico 自带消费规则，此处仅禁用相关警告
-dontwarn androidx.compose.material3.**
-dontwarn com.patrykandpatrick.vico.**

# 保留枚举的 values/valueOf
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
