plugins {
    id("com.android.application") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
}

val localBuildRoot = file("${System.getProperty("user.home")}/.gradle/namma-shaale-inventory-build")

allprojects {
    val projectBuildName = if (path == ":") "root" else path.trimStart(':').replace(':', '-')
    layout.buildDirectory.set(localBuildRoot.resolve(projectBuildName))
}
