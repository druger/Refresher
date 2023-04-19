plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.druger.refresher"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    kapt {
        correctErrorTypes = true
    }

    packagingOptions.resources.excludes.add("META-INF/*")
}

dependencies {
    val coroutinesTest = "1.6.4"
    val junit = "5.8.1"
    val room = "2.5.1"
    val testKtx = "1.5.0"
    val testRules = "1.5.0"
    val testRunner = "1.5.2"

    implementation(project(path = ":domain"))

    // Room
    implementation("androidx.room:room-runtime:$room")
    implementation("androidx.room:room-ktx:$room")
    kapt("androidx.room:room-compiler:$room")

    // Test
    androidTestImplementation("org.junit.jupiter:junit-jupiter:$junit")
    testImplementation("androidx.room:room-testing:$room")
    implementation("androidx.test:core-ktx:$testKtx")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesTest")
    androidTestImplementation("androidx.test:runner:$testRunner")
    androidTestImplementation("androidx.test:rules:$testRules")
}

tasks.withType<Test> {
    useJUnitPlatform()
}