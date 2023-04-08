plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    val coroutinesTest = "1.6.4"
    val junit = "4.13.2"
    val mockito ="4.11.0"
    val mockitoKotlin ="4.1.0"

    testImplementation("junit:junit:$junit")
    testImplementation ("org.mockito:mockito-core:$mockito")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:$mockitoKotlin")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesTest")
}