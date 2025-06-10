plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "kr.ac.yuhan.cs.androidproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "kr.ac.yuhan.cs.androidproject"
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
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.4")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")

    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.libraries.places:places:3.3.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
}
