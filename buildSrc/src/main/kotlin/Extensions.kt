fun String.isStableVersion(): Boolean {
    val upperCase = toUpperCase(java.util.Locale.ROOT)
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { it in upperCase }
    return stableKeyword || Regex("^[0-9,.v-]+(-r)?$").matches(this)
}

fun String.isNotStableVersion() = !isStableVersion()