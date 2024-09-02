import org.gradle.internal.impldep.bsh.commands.dir

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    defaultConfig {
        applicationId = "com.zwstudio.lolly"
        minSdk = 24
        compileSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"))
            // Enable debugging
            // isDebuggable = true
            // signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    productFlavors {
    }
    buildFeatures {
        dataBinding = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "com.zwstudio.lolly"
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    implementation(project(path = ":lollylib"))
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("javax.persistence:persistence-api:1.0.2")
    implementation("com.github.woxthebox:draglistview:1.7.3")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.navigation:navigation-fragment-ktx:${rootProject.extra["nav_version"]}")
    implementation("androidx.navigation:navigation-ui-ktx:${rootProject.extra["nav_version"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("androidx.fragment:fragment-ktx:1.8.2")
    implementation("io.insert-koin:koin-android:${rootProject.extra["koin_version"]}")
    // debugImplementation(because LeakCanary should only run in debug builds.)
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")
}
