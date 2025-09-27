plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    gradlePluginPortal()
    google()
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(libs.plugins.androidBuildTools.get().toString())
}