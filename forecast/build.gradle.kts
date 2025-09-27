import cszsm.dolgok.buildSrc.plugins.ModuleBuildConfigPlugin

plugins { alias(libs.plugins.androidLibrary) }

apply<ModuleBuildConfigPlugin>()

android { namespace = "cszsm.dolgok.forecast" }

dependencies {
    // TODO: move network calls and these dependencies to a separate module
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.resources)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)

    // modules
    implementation(project(":core"))
    implementation(project(":localization"))
}