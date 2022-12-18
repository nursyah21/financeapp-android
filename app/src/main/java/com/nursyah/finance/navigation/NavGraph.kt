package com.nursyah.finance.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.nursyah.finance.core.Constants.SCREEN_HOME
import com.nursyah.finance.core.Constants.SCREEN_LICENSE
import com.nursyah.finance.core.Constants.SCREEN_SETTINGS
import com.nursyah.finance.core.Constants.SCREEN_STATS
import com.nursyah.finance.presentation.screens.home.HomeScreen
import com.nursyah.finance.presentation.screens.license.LicenseScreen
import com.nursyah.finance.presentation.screens.settings.SettingsScreen
import com.nursyah.finance.presentation.screens.stats.StatsScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
  navController: NavHostController
) {
  val ctx = LocalContext.current
  AnimatedNavHost(navController = navController, startDestination = ctx.getString(SCREEN_HOME)){
    composable(ctx.getString(SCREEN_HOME)) { HomeScreen() }
    composable(ctx.getString(SCREEN_STATS)) { StatsScreen() }
    composable(ctx.getString(SCREEN_SETTINGS)) { SettingsScreen(navHostController = navController) }
    composable(ctx.getString(SCREEN_LICENSE)) { LicenseScreen(navHostController = navController) }
  }
}