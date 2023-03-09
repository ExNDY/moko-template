package org.example.app.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.example.app.features.config.ConfigScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootContainer() {
    val navController: NavHostController = rememberAnimatedNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.padding(it)) {
            AnimatedNavHost(
                navController = navController,
                startDestination = getScreenName<ConfigScreen>()
            ) {
                val enterTransition: AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition? =
                    {
                        slideIntoContainer(
                            towards = AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(400)
                        )
                    }

                val exitTransition: AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition? =
                    {
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(400)
                        )
                    }

                val popExitTransition: AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition? =
                    {
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    }

                val popEnterTransition: AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition? =
                    {
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(350)
                        )
                    }

                allScreens.forEach { screen ->
                    composable(
                        enterTransition = enterTransition,
                        exitTransition = exitTransition,
                        popEnterTransition = popEnterTransition,
                        popExitTransition = popExitTransition,
                        route = screen.screenName,
                        arguments = screen.navArgs,
                    ) { backStackEntry ->
                        val args = backStackEntry.arguments

                        screen.Content(navController = navController, args = args)
                    }
                }
            }
        }
    }
}
