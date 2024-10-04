import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("com.vanniktech.maven.publish") version "0.28.0"
    id("com.gradleup.nmcp") version "0.0.7"


}

group = "io.github.mtlabdo"
version = "1.0"

kotlin {
    targetHierarchy.default()
    androidTarget {

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        publishLibraryVariants("release", "debug")
    }
    jvm("desktop")
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "printerLib"
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(libs.ktor.network)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
        }
        desktopMain.dependencies {
        }
        iosMain.dependencies {
        }
    }
}

android {
    namespace = "io.github.mtlabdo.escprinterlib"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}


mavenPublishing {
    coordinates(
        groupId = "io.github.mtlabdo",
        artifactId = "escprinterlib",
        version = "1.0.0"
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("Printer Lib KMP")
        description.set("Library used to open up a web browser on both Android/iOS.")
        inceptionYear.set("2024")
        url.set("https://github.com/mtlabdo/PrinterKMP")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        // Specify developers information
        developers {
            developer {
                id.set("mtlabdo")
                name.set("abdo mtl")
                email.set("test@gmail.com")
            }
        }

        // Specify SCM information
        scm {
            url.set("https://github.com/mtlabdo/PrinterKMP")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}
task("testClasses") {}
