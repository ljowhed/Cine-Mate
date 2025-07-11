plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.cinemate"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cinemate"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation ("com.google.android.material:material:1.10.0")
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation ("com.android.volley:volley:1.2.1")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}