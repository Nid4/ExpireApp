plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

}

android {
    namespace = "com.kasjan"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kasjan"
        minSdk = 27
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
    kotlinOptions {
        jvmTarget = "11"


    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Room runtime
    implementation (libs.androidx.room.runtime)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.firebase.database.ktx)
    // Kompilator Room z KSP
    ksp (libs.androidx.room.compiler)
    // Opcjonalne: Room KTX
    implementation (libs.androidx.room.ktx)

    //Camera
    implementation (libs.barcode.scanning)
    implementation ("androidx.camera:camera-camera2:1.0.0")
    implementation ("androidx.camera:camera-lifecycle:1.0.0")
    implementation ("androidx.camera:camera-view:1.0.0") // Jeśli chcesz używać CameraX PreviewView
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1") // Dla LifecycleOwner

// Import the Firebase BoM

    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    implementation ("androidx.work:work-runtime-ktx:2.10.0")

    implementation(libs.play.services.analytics.impl)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}