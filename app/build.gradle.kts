import cszsm.dolgok.buildSrc.plugins.ApplicationConfigPlugin

plugins { alias(libs.plugins.androidApplication) }

apply<ApplicationConfigPlugin>()

android { namespace = "cszsm.dolgok" }

dependencies {
    // modules
    implementation(project(":core"))
    implementation(project(":localization"))
    implementation(project(":home"))
    implementation(project(":forecast"))
    implementation(project(":animation"))
}