@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    compilerOptions {
        jvm {
            version = JavaVersion.VERSION_11
        }
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    applyDefaultHierarchyTemplate {
        common {
            group("mobile") {
                group("android")
                group("ios")
            }

            group("noMobile") {
                withJvm()
                withWasmJs()
            }
        }
    }

    androidLibrary {
        namespace = "org.danieh.tmdb.data.database"
        compileSdk = 36
        minSdk = 31

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "databaseKit"
        }
    }

    sourceSets {
        val commonMain by getting
        commonMain.dependencies {
            implementation(projects.domain)
            implementation(libs.arrow.core)
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        val mobileMain by getting {
            dependsOn(commonMain)
        }
        mobileMain.dependencies {
            implementation(libs.roomRuntime)
            implementation(libs.sqlite.bundled)
        }

        androidMain {
            dependsOn(mobileMain)
            dependencies {
                implementation(libs.roomKtx)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
                implementation(libs.roomTesting)
            }
        }

        iosMain {
            dependsOn(mobileMain)
            dependencies {
            }
        }
    }

}

room {
    schemaDirectory("$projectDir/schemas")
}

// Should be its own top level block. For convenience, add at the bottom of the file
dependencies {
    add("kspAndroid", libs.roomCompiler)
    add("kspIosSimulatorArm64", libs.roomCompiler)
    add("kspIosX64", libs.roomCompiler)
    add("kspIosArm64", libs.roomCompiler)
}
