/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("android-app-convention")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    defaultConfig {
        applicationId = "org.example.app"

        versionCode = 1
        versionName = "0.1.0"

        val url = "https://newsapi.org/v2/"
        buildConfigField("String", "BASE_URL", "\"$url\"")
    }
}

dependencies {
    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.recyclerView)
    implementation(libs.swipeRefreshLayout)
    implementation(libs.splashScreen)

    // Compose
    implementation(platform(libs.composeBom))
    implementation(libs.composeMaterial)
    implementation(libs.composeMaterialIcons)
    implementation(libs.composeFoundation)
    implementation(libs.composeFoundationLayout)
    implementation(libs.composeConstraintLayout)
    implementation(libs.composeUiPreview)
    implementation(libs.composeActivity)
    implementation(libs.composeNavigation)
    implementation(libs.composeLivedata)
    implementation(libs.composeCoil)
    implementation(libs.mokoMvvmLiveDataCompose)
    implementation(libs.mokoMvvmFlowCompose)
    implementation(libs.mokoResourcesCompose)
    implementation(libs.accompanistNavigationAnimation)

    // Navigation
    implementation(libs.navigationComponent)
    implementation(libs.navigationUIComponent)

    // Hilt
    implementation(libs.hilt)
    kapt(libs.hiltCompiler)

    implementation(projects.mppLibrary)
    // Debug
    debugImplementation(libs.composeUiTooling)
}
