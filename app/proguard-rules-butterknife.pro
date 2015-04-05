-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }

-keepclasseswithmembernames class * {
    @butterknife.* ;
}

-keepclasseswithmembernames class * {
    @butterknife.* ;
}