// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle)
        classpath(kotlin("gradle-plugin", version = "2.0.20"))
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
plugins {
    alias(libs.plugins.compose.compiler) apply false
}
