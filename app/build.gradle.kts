plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.druger.refresher"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles (getDefaultProguardFile ("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    kapt {
        correctErrorTypes = true
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
    }
    namespace = "com.druger.refresher"
}



dependencies {
    val kotlinVersion = "1.8.10"
    val activityCompose = "1.6.1"
    val composeBom = platform("androidx.compose:compose-bom:2023.03.00")
    val coroutines = "1.3.5"
    val dagger = "2.45"
    val daggerCompiler = "2.45"
    val ktx = "1.9.0"
    val lifecycle = "2.6.1"
    val material = "1.4.0"
    val navigationCompose = "2.5.3"
    val viewModelCompose = "2.6.1"
    val room = "2.5.1"

    implementation(project(path = ":domain"))
    implementation(project(path = ":data"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // AndroidX
    implementation("androidx.core:core-ktx:$ktx")
    // UI
    implementation("com.google.android.material:material:$material")
    // Compose
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.activity:activity-compose:$activityCompose")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$viewModelCompose")
    implementation("androidx.compose.runtime:runtime-livedata")
    // DI
    implementation("com.google.dagger:dagger:$dagger")
    kapt("com.google.dagger:dagger-compiler:$daggerCompiler")
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle")
    // Room
    implementation("androidx.room:room-runtime:$room")
    implementation("androidx.room:room-ktx:$room")
    kapt("androidx.room:room-compiler:$room")
    // Coroutines
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")
    // Navigation
    implementation("androidx.navigation:navigation-compose:$navigationCompose")
}

repositories {
    mavenCentral()
}