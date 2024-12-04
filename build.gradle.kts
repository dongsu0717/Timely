plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.navigation.safeargs) apply false
//    id("androidx.navigation.safeargs") version "2.8.2" apply false
    alias(libs.plugins.google.gms.google.services) apply false
}
