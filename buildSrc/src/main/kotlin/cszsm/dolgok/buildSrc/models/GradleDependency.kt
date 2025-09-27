package cszsm.dolgok.buildSrc.models

data class GradleDependency(
    val configuration: GradleDependencyConfiguration,
    val alias: String,
    val platformDependency: Boolean = false,
)

enum class GradleDependencyConfiguration(
    val configurationName: String,
) {
    IMPLEMENTATION("implementation"),
    TEST_IMPLEMENTATION("testImplementation"),
    ANDROID_TEST_IMPLEMENTATION("androidTestImplementation"),
    DEBUG_IMPLEMENTATION("debugImplementation"),
}