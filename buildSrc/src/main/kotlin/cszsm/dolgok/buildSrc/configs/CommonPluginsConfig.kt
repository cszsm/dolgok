package cszsm.dolgok.buildSrc.configs

import cszsm.dolgok.buildSrc.extensions.getPluginId
import org.gradle.api.Project

internal fun Project.configureCommonPlugins() {
    apply {
        plugin(project.getPluginId("jetbrainsKotlinAndroid"))
        plugin(project.getPluginId("kotlinSerialization"))
        plugin(project.getPluginId("composeCompiler"))
    }
}