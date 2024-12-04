import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}



android {
    namespace = "com.dongsu.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val tMapApiKey = (properties["TMAP_API_KEY"] as? String)?.trim('"') ?: ""
        buildConfigField("String", "TMAP_API_KEY", "\"$tMapApiKey\"")

        val timelyHostURL = (properties["TIMELY_HOST_URL"] as? String)?.trim('"') ?: ""
        buildConfigField("String", "TIMELY_HOST_URL", "\"$timelyHostURL\"")
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
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //gms
    implementation(libs.play.services.location)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //annotations
    implementation(libs.annotations)

    //hilt
//    implementation (libs.hilt.android)
    implementation(libs.google.hilt)
    kapt(libs.hilt.compiler)

    // Networking
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)

    // Tmap SDK
//    implementation(files("libs/vsm-tmap-sdk-v2-android-1.6.60.aar"))
//    implementation(files("libs/tmap-sdk-1.5.aar"))
}
kapt {
    correctErrorTypes = true
}