/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("multiplatform-library-convention")
    id("kotlin-parcelize")
}

dependencies {
    commonMainImplementation(libs.coroutines)

    androidMainImplementation(libs.lifecycleViewModel)
    androidMainImplementation(libs.recyclerView)
    androidMainImplementation(libs.mokoMvvmLiveDataMaterial)

    commonMainImplementation(libs.mokoMvvmLiveData)
    commonMainImplementation(libs.mokoMvvmState)
    commonMainImplementation(libs.mokoResources)
    commonMainApi(libs.mokoUnits)
    commonMainImplementation(libs.mokoUnitsBasic)

    commonMainImplementation(libs.napier)
}
