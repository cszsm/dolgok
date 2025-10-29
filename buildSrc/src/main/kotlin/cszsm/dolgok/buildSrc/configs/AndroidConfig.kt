package cszsm.dolgok.buildSrc.configs

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

internal fun Project.configureAndroid(extension: CommonExtension<*, *, *, *, *, *>) {
    extension.apply {
        compileSdk = 36

        defaultConfig {
            minSdk = 34

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            vectorDrawables {
                useSupportLibrary = true
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        buildFeatures {
            compose = true
        }

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }
}