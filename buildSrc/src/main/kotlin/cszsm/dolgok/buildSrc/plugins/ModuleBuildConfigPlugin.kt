package cszsm.dolgok.buildSrc.plugins

import com.android.build.api.dsl.LibraryExtension
import cszsm.dolgok.buildSrc.configs.configureAndroid
import cszsm.dolgok.buildSrc.configs.configureCommonDependencies
import cszsm.dolgok.buildSrc.configs.configureCommonPlugins
import cszsm.dolgok.buildSrc.configs.configurePackaging
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ModuleBuildConfigPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configure<LibraryExtension> {
                configureCommonPlugins()
                configureAndroid(extension = this)
                configurePackaging(extension = this)
                configureCommonDependencies()
            }
        }
    }
}