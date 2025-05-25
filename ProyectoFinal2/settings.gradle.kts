pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        // Acceder a las propiedades usando providers
        val kotlinVersion = providers.gradleProperty("kotlin.version").get()
        val composeVersion = providers.gradleProperty("compose.version").get()

        kotlin("jvm").version(kotlinVersion)
        id("org.jetbrains.compose").version(composeVersion)
        id("org.jetbrains.kotlin.plugin.compose").version(kotlinVersion)
        id("org.jetbrains.kotlin.plugin.serialization").version(kotlinVersion)
    }
}

rootProject.name = "ProyectoFinal"
