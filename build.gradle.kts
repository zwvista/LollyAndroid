// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("kotlin_version", "1.9.0")
        set("nav_version", "2.7.7")
        set("retrofit_version", "2.11.0")
        set("lifecycle_version", "2.8.4")
        set("koin_version", "3.5.6")
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.5.2")
        classpath(kotlin("gradle-plugin", version = "${rootProject.extra["kotlin_version"]}"))
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${rootProject.extra["nav_version"]}")
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
