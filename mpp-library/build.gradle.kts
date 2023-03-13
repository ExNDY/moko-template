/*
 * Copyright 2023 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("multiplatform-library-convention")
    id("dev.icerock.mobile.multiplatform-resources")
    id("dev.icerock.mobile.multiplatform.ios-framework")
    id("dev.icerock.moko.kswift")
}

dependencies {
    commonMainImplementation(libs.coroutines)
    commonMainImplementation(libs.ktorClient)

    androidMainImplementation(libs.multidex)
    androidMainImplementation(libs.lifecycleViewModel)

    commonMainApi(libs.multiplatformSettings)
    commonMainApi(libs.napier)
    commonMainApi(libs.mokoParcelize)
    commonMainApi(libs.mokoResources)
    commonMainApi(libs.mokoMvvmCore)
    commonMainApi(libs.mokoMvvmLiveData)
    commonMainApi(libs.mokoMvvmLiveDataResources)
    commonMainApi(libs.mokoMvvmState)
    commonMainImplementation(libs.mokoUnitsBasic)
    commonMainApi(libs.mokoFieldsCore)
    commonMainApi(libs.mokoFieldsLiveData)

    commonMainApi(projects.mppLibrary.domain)
    commonMainApi(projects.mppLibrary.feature.config)
    commonMainApi(projects.mppLibrary.feature.list)

    commonTestImplementation(libs.mokoTestCore)
    commonTestImplementation(libs.mokoMvvmTest)
    commonTestImplementation(libs.multiplatformSettingsTest)
    commonTestImplementation(libs.ktorClientMock)
}

multiplatformResources {
    multiplatformResourcesPackage = "org.example.library"
}

framework {
    export(projects.mppLibrary.domain)
    export(projects.mppLibrary.feature.config)
    export(projects.mppLibrary.feature.list)

    export(libs.multiplatformSettings)
    export(libs.napier)
    export(libs.mokoParcelize)
    export(libs.mokoResources)
    export(libs.mokoMvvmCore)
    export(libs.mokoMvvmLiveData)
    export(libs.mokoMvvmLiveDataResources)
    export(libs.mokoMvvmState)
    export(libs.mokoUnits)
    export(libs.mokoUnitsBasic)
    export(libs.mokoFieldsCore)
    export(libs.mokoFieldsLiveData)
}

/*
    fix Moko-Resources for Android Gradle Plugin 7.3.0 (can remove after update to AGP 8.0+)
    https://github.com/icerockdev/moko-resources/issues/353
 */
android {
    sourceSets["main"].apply {
        assets.srcDir(File(buildDir, "generated/moko/androidMain/assets"))
        res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
    }
}

kswift {
    install(dev.icerock.moko.kswift.plugin.feature.SealedToSwiftEnumFeature)
    install(dev.icerock.moko.kswift.plugin.feature.PlatformExtensionFunctionsFeature) {
        filter = excludeFilter(
            "PackageFunctionContext/dev.icerock.moko:mvvm-state/dev.icerock.moko.mvvm/TypeParameter(id=0)/asState/",
            "PackageFunctionContext/dev.icerock.moko:mvvm-state/dev.icerock.moko.mvvm/TypeParameter(id=0)/asState/whenNull:Class(name=kotlin/Function0)<Class(name=dev/icerock/moko/mvvm/ResourceState)<TypeParameter(id=0),TypeParameter(id=1)>>",
        )
    }

    excludeLibrary("ktor-io")
    excludeLibrary("ktor-websocket-serialization")
    excludeLibrary("ktor-websockets")
    excludeLibrary("ktor-client-core")
    excludeLibrary("ktor-serialization")
    excludeLibrary("ktor-http")
    excludeLibrary("ktor-utils")
    excludeLibrary("kotlinx-coroutines-core")
    excludeLibrary("kotlinx-serialization-core")
    excludeLibrary("kotlinx-serialization-json")
    excludeLibrary("mvvm-flow")
    excludeLibrary("kotlinx-datetime")
    excludeLibrary("date-time")
    excludeLibrary("multiplatform-settings")
    excludeLibrary("errors")
    excludeLibrary("fields")
    excludeLibrary("mvvm-flow")
    excludeLibrary("moko-units")
    excludeLibrary("network")
    excludeLibrary("network-errors")
    excludeLibrary("resources")

    projectPodspecName.set("MultiPlatformLibrary")
    iosDeploymentTarget.set("12.0")
}

kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
    binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>().configureEach {
        embedBitcodeMode.set(org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode.DISABLE)
    }
}

kotlin.targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
    binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>().configureEach {
        linkTask.doLast {
            val from = File(outputDirectory, "${baseName}Swift").takeIf { it.exists() } ?: return@doLast
            val to = File(rootDir, "ios-app/kswift")
            from.copyRecursively(to, overwrite = true)
        }
    }
}
