package com.nursyah.finance.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.nursyah.finance.presentation.screens.home.HomeScreen
import com.nursyah.finance.presentation.screens.license.LicenseScreen
import com.nursyah.finance.presentation.screens.settings.HowToUseScreen
import com.nursyah.finance.presentation.screens.settings.SettingsScreen
import com.nursyah.finance.presentation.screens.stats.StatsScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    navController: NavHostController
) {
    val ctx = LocalContext.current
    AnimatedNavHost(navController = navController, startDestination = MainDestination.Home.route) {
        composable(route = MainDestination.Home.route) { HomeScreen() }
        composable(route = MainDestination.Stats.route) { StatsScreen(navHostController = navController) }
        composable(route = MainDestination.License.route) { LicenseScreen(navHostController = navController) }
        navigation(
            route = MainDestination.SettingsRoute.route,
            startDestination = MainDestination.SettingsRoute.Settings.route
        ) {
            composable(route = MainDestination.SettingsRoute.Settings.route) {
                SettingsScreen(
                    navHostController = navController
                )
            }
            composable(route = MainDestination.SettingsRoute.HowToUse.route) {
                HowToUseScreen(
                    navHostController = navController
                )
            }
        }
    }
}

sealed class MainDestination(val route: String) {
    object Home : MainDestination(route = SCREEN_HOME)
    object Stats : MainDestination(route = SCREEN_STATS)
    object SettingsRoute : MainDestination(route = SETTINGS_ROUTE) {
        object Settings : MainDestination(route = SCREEN_SETTINGS)
        object HowToUse : MainDestination(route = SCREEN_HTU)
    }

    object License : MainDestination(route = SCREEN_LICENSE)
}

val SCREEN_HOME = "home"
val SCREEN_STATS = "stats"
val SETTINGS_ROUTE = "settings-route"
val SCREEN_SETTINGS = "settings"
val SCREEN_LICENSE = "license"
val SCREEN_HTU = "howtouse"