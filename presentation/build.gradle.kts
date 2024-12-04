plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    alias(libs.plugins.navigation.safeargs)
//    id("androidx.navigation.safeargs")
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
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //hilt
    implementation(libs.google.hilt)
    kapt(libs.hilt.compiler)

    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //flowbinding bundle
    implementation(libs.bundles.flowbindings)

    //calendar library
    implementation(libs.view)

    //annotations
    implementation(libs.annotations)

    //kakao login, share
    implementation (libs.v2.user)
    implementation (libs.v2.share)

    //kakao map
    implementation (libs.android)

    //lottie
    implementation (libs.lottie)

    //glide
    implementation (libs.glide)
    kapt (libs.compiler)

    //ted permission
    implementation(libs.tedpermission.normal)
}
kapt {
    correctErrorTypes = true
}