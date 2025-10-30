import cszsm.dolgok.buildSrc.plugins.ModuleBuildConfigPlugin

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

apply<ModuleBuildConfigPlugin>()

android { namespace = "cszsm.dolgok.pointandshoot" }

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(libs.camerax.core)
    implementation(libs.camerax.camera)
    implementation(libs.camerax.lifecycle)
    implementation(libs.camerax.compose)
    implementation(libs.androidx.constraintlayout)

    // modules
    implementation(project(":core"))
    implementation(project(":localization"))
}