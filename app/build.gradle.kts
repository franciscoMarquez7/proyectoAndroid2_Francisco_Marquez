plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}


android {
    namespace = "com.example.proyecto_francisco_marquez"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.proyecto_francisco_marquez"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17 // Cambiado a Java 17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17" // Cambiado a Java 17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.ui:ui:1.5.3")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("androidx.compose.material:material-icons-extended:1.5.0")
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.firebase.common.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.auth.ktx)
    implementation("com.google.firebase:firebase-auth-ktx:21.3.0")
    implementation("com.google.android.gms:play-services-auth:20.6.0")
    implementation("io.coil-kt:coil-compose:2.2.2")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
