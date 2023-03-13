/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package org.example.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.example.app.di.AppComponent
import org.example.library.SharedFactory
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {
    @Inject
    lateinit var factory: SharedFactory

    override fun onCreate() {
        super.onCreate()
        AppComponent.initialize(factory)
    }
}
