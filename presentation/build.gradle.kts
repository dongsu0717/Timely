plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.dongsu.presentation"
    compileSdk = 34

    defaultConfig {
//        applicationId = "com.dongsu.timely"
        minSdk = 26
        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(project(":common"))
    implementation(project(":domain"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //hilt
    implementation(libs.google.hilt)
    kapt(libs.hilt.compiler)

    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //flowbinding
    implementation(libs.bundles.flowbindings)

    //calendar library
    implementation("com.kizitonwose.calendar:view:2.6.0")

    //annotations. 해야 중복방지
    implementation("org.jetbrains:annotations:23.0.0")

    //kakao login, share
    implementation ("com.kakao.sdk:v2-user:2.20.6")
    implementation ("com.kakao.sdk:v2-share:2.20.6")

    //kakao map
    implementation ("com.kakao.maps.open:android:2.12.8")

    //lottie
    implementation ("com.airbnb.android:lottie:6.6.0")

    //glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    kapt ("com.github.bumptech.glide:compiler:4.16.0")

    //ted permission
    implementation("io.github.ParkSangGwon:tedpermission-normal:3.4.2")
}
kapt {
    correctErrorTypes = true
}