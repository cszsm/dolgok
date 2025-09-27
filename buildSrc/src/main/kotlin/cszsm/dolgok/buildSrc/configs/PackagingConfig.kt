package cszsm.dolgok.buildSrc.configs

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configurePackaging(extension: CommonExtension<*, *, *, *, *, *>) {
    extension.apply {
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}