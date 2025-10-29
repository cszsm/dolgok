package cszsm.dolgok.buildSrc.plugins

import com.android.build.api.dsl.ApplicationExtension
import cszsm.dolgok.buildSrc.configs.configureAndroid
import cszsm.dolgok.buildSrc.configs.configureBuildTypes
import cszsm.dolgok.buildSrc.configs.configureCommonDependencies
import cszsm.dolgok.buildSrc.configs.configureCommonPlugins
import cszsm.dolgok.buildSrc.configs.configurePackaging
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ApplicationConfigPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = "cszsm.dolgok"
                    targetSdk = 36
                    versionCode = 1
                    versionName = "1.0"
                }

                configureCommonPlugins()
                configureBuildTypes()
                configureAndroid(extension = this)
                configurePackaging(extension = this)
                configureCommonDependencies()
            }
        }
    }
}