package com.lucianbc.receiptscan.buildsrc

import org.gradle.api.JavaVersion

object Config {
    const val minSdk = 23
    const val compileSdk = 28
    const val targetSdk = 28
    val javaVersion = JavaVersion.VERSION_1_8
    const val buildTools = "28.0.3"
}

object Dependencies {
    object Google {
        const val material = "com.google.android.material:material:1.2.0-alpha04"
    }

    object Androidx {
        private const val navVersion = "2.3.0"
        const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${navVersion}"
        const val navigationUi = "androidx.navigation:navigation-ui-ktx:${navVersion}"
        const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${navVersion}"
    }
}