package cszsm.dolgok.buildSrc.extensions

import cszsm.dolgok.buildSrc.models.GradleDependency
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

internal fun Project.getLibrary(dependency: GradleDependency): Provider<MinimalExternalModuleDependency> {
    return libraries.findLibrary(dependency.alias).get()
}

internal fun Project.getPluginId(pluginAlias: String): String {
    return libraries.findPlugin(pluginAlias).get().get().pluginId
}

private val Project.libraries: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")
