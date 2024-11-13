import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    alias(libs.plugins.google.gms.google.services)

}

val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
fun String.removeQuotes(): String {
    return this.replace("\"", "")
}

android {
    namespace = "com.dongsu.timely"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dongsu.timely"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val kakaoApiKey = (properties["KAKAO_API_KEY"] as? String)?.trim('"') ?: ""
        buildConfigField("String", "KAKAO_API_KEY", "\"$kakaoApiKey\"")
        manifestPlaceholders["kakaoApiKey"] = kakaoApiKey
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))
    implementation(project(":service"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //hilt
    implementation(libs.google.hilt)
    kapt(libs.hilt.compiler)

    //kakao login, share
    implementation ("com.kakao.sdk:v2-user:2.20.6")
    implementation ("com.kakao.sdk:v2-share:2.20.6")

    //kakao map
    implementation ("com.kakao.maps.open:android:2.12.8")
    
    //fcm
    implementation(libs.firebase.messaging)



}