package com.nursyah.finance.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.nursyah.finance.core.Constants.SCREEN_HOME
import com.nursyah.finance.navigation.NavGraph
import com.nursyah.finance.presentation.components.Navbar
import com.nursyah.finance.presentation.theme.FinanceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  private lateinit var navController: NavHostController
  @OptIn(ExperimentalAnimationApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      navController = rememberAnimatedNavController()
      FinanceTheme {
        App(navController)
      }
    }
  }
}

@Composable
fun App(navController: NavHostController) {
  val ctx = LocalContext.current
  var navbarSelected by rememberSaveable{ mutableStateOf(ctx.getString(SCREEN_HOME)) }

  Scaffold(
    bottomBar = {
      Navbar(
        onClick = {navbarSelected = it},
        value = navbarSelected,
        navController = navController,
      )
    }
  ) {
    Column(modifier = Modifier.padding(it)) {
      NavGraph(navController = navController)
    }
  }
}

