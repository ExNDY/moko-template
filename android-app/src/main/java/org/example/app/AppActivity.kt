package org.example.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import org.example.app.navigation.RootContainer
import org.example.library.SharedFactory
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : FragmentActivity() {
    @Inject lateinit var factory: SharedFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                RootContainer()
            }
        }
    }
}
