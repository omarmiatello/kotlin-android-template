/**
 * A plugin to cleanup the template after it has been forked. It register a single `templateCleanup`
 * task that is designed to run from CI. It:
 * - renames the root project
 * - replaces the maven coordinates with coordinates based on the Github repository where the
 * template is forked
 * - changes the package name
 * - changes the Android application ID
 * - cleanups after itself by removing the Github action and this plugin
 */

check(rootProject.name == name) {
    "The cleanup plugin should be applied to the root project and not $name"
}

tasks.register("templateCleanup") {
    val repository = System.getenv("GITHUB_REPOSITORY")
        ?: error("No GITHUB_REPOSITORY environment variable. Are you running from Github Actions?")
    val (owner, name) = repository.split("/")
    val ownerForPackage = owner.filter { it.isLetter() }.ifEmpty { "owner" }
    val nameForPackage = name.filter { it.isLetter() }.ifEmpty { "template" }

    fun File.editText(block: (String) -> String) = writeText(block(readText()))

    file("settings.gradle.kts").editText { it.replace("android-template", name) }

    file("buildSrc/src/main/kotlin/publish.gradle.kts").editText {
        it.replace("owner/template", repository)
            .replace("owner", owner)
    }

    file("README.md").editText { readmeText ->
        file(".github/template-cleanup/README.md")
            .readText()
            .replace("%NAME%", name)
            .replace("%REPOSITORY%", repository) +
                readmeText.replaceBefore("## Features", "")
    }

    // rename folders + fix package/imports
    projectDir
        .listFiles { file -> file.isDirectory && file.name != "buildSrc" }!!
        .forEach { dir ->
            dir.walkBottomUp().forEach { file ->
                when {
                    file.isDirectory -> {                               // rename folders
                        when (file.name) {
                            "owner" -> file.renameTo(file.resolveSibling(ownerForPackage))
                            "template" -> file.renameTo(file.resolveSibling(nameForPackage))
                        }
                    }
                    file.extension in listOf("kt", "kts", "xml") -> {   // fix package/imports
                        file.editText {
                            val nameCap = nameForPackage.capitalize()
                            it.replace("owner.template", "$ownerForPackage.$nameForPackage")
                                .replace("TemplateTheme", "${nameCap}Theme")
                                .replace("TemplateColor", "${nameCap}Color")
                                .replace("Theme.Template", "Theme.${nameCap}")
                                .replace("Android Template", name)
                        }
                    }
                }
            }
        }

    // cleanup the cleanup :)
    file(".github/template-cleanup").deleteRecursively()
    file(".github/workflows/cleanup.yaml").delete()
    file("buildSrc/src/main/kotlin/cleanup.gradle.kts").delete()
    file("build.gradle.kts").editText {
        it.replace("    cleanup\n", "")
            .replace("owner.template", "$ownerForPackage.$nameForPackage")
    }
}
