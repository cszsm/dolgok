import cszsm.dolgok.buildSrc.plugins.ModuleBuildConfigPlugin

plugins { alias(libs.plugins.androidLibrary) }

apply<ModuleBuildConfigPlugin>()

android { namespace = "cszsm.dolgok.core" }

dependencies {
    // modules
    implementation(project(":localization"))
}