package cszsm.dolgok.buildSrc.configs

import cszsm.dolgok.buildSrc.extensions.getLibrary
import cszsm.dolgok.buildSrc.models.GradleDependency
import cszsm.dolgok.buildSrc.models.GradleDependencyConfiguration
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

private val commonDependencies = listOf(
    implementation("androidx.core.ktx"),
    implementation("androidx.lifecycle.runtime.ktx"),
    implementation("androidx.activity.compose"),
    implementation("androidx.compose.bom", platformDependency = true),
    implementation("androidx.ui"),
    implementation("androidx.ui.graphics"),
    implementation("androidx.ui.tooling.preview"),
    implementation("androidx.material3"),
    implementation("androidx.navigation.compose"),
    implementation("androidx.navigation.dynamic.features.fragment"),
    implementation("kotlinx.serialization.json"),
    implementation("kotlinx.datetime"),
    implementation("koin.android"),
    implementation("koin.compose"),

    testImplementation("junit.core"),
    testImplementation("junit.platform.engine"),
    testImplementation("junit.platform.launcher"),
    testImplementation("junit.jupiter"),
    testImplementation("mockk"),
    testImplementation("kotlinx.coroutines.test"),

    androidTestImplementation("androidx.junit"),
    androidTestImplementation("androidx.espresso.core"),
    androidTestImplementation("androidx.compose.bom", platformDependency = true),
    androidTestImplementation("androidx.ui.test.junit4"),
    androidTestImplementation("androidx.navigation.testing"),

    debugImplementation("androidx.ui.tooling"),
    debugImplementation("androidx.ui.test.manifest"),
)

internal fun Project.configureCommonDependencies() {
    dependencies {
        commonDependencies.forEach { dependency ->
            val dependencyNotation =
                if (dependency.platformDependency) {
                    platform(getLibrary(dependency))
                } else {
                    getLibrary(dependency)
                }

            add(
                configurationName = dependency.configuration.configurationName,
                dependencyNotation = dependencyNotation
            )
        }
    }
}

private fun implementation(alias: String, platformDependency: Boolean = false) =
    GradleDependency(
        configuration = GradleDependencyConfiguration.IMPLEMENTATION,
        alias = alias,
        platformDependency = platformDependency,
    )

private fun testImplementation(alias: String, platformDependency: Boolean = false) =
    GradleDependency(
        configuration = GradleDependencyConfiguration.TEST_IMPLEMENTATION,
        alias = alias,
        platformDependency = platformDependency,
    )

private fun androidTestImplementation(alias: String, platformDependency: Boolean = false) =
    GradleDependency(
        configuration = GradleDependencyConfiguration.ANDROID_TEST_IMPLEMENTATION,
        alias = alias,
        platformDependency = platformDependency,
    )

private fun debugImplementation(alias: String, platformDependency: Boolean = false) =
    GradleDependency(
        configuration = GradleDependencyConfiguration.DEBUG_IMPLEMENTATION,
        alias = alias,
        platformDependency = platformDependency,
    )
