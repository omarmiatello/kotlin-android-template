import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    kotlin("android") apply false
    id("io.gitlab.arturbosch.detekt") version Version.pluginDetekt
    id("org.jlleitschuh.gradle.ktlint") version Version.pluginKtlint
    id("com.github.ben-manes.versions") version Version.pluginBenManesVersions
    cleanup
}

allprojects {
    group = "com.github.owner.template"
    repositories {
        mavenCentral()
        google()
        jcenter()   // JCenter is at end of life
    }
}

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("com.github.ben-manes.versions")
    }

    ktlint {
        debug.set(false)
        version.set(Version.ktlint)
        verbose.set(true)
        android.set(false)
        outputToConsole.set(true)
        ignoreFailures.set(false)
        enableExperimentalRules.set(true)
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }

    detekt {
        config = rootProject.files("config/detekt/detekt.yml")
        reports {
            html {
                enabled = true
                destination = file("build/reports/detekt.html")
            }
        }
    }
}

tasks {
    register("clean", Delete::class.java) {
        delete(rootProject.buildDir)
    }

    withType<DependencyUpdatesTask> {
        rejectVersionIf {
            candidate.version.isStableVersion().not()
        }
    }
}
