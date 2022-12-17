package com.nursyah.finance.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.nursyah.finance.R
import com.nursyah.finance.presentation.components.Navbar
import com.nursyah.finance.presentation.screens.home.HomeScreen
import com.nursyah.finance.presentation.screens.settings.SettingsScreen
import com.nursyah.finance.presentation.screens.stats.StatsScreen
import com.nursyah.finance.presentation.theme.FinanceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      FinanceTheme {
        App()
      }
    }
  }

}

@Composable
fun App() {
  val ctx = LocalContext.current
  var navbarSelected by remember { mutableStateOf(ctx.getString(R.string.home)) }
  Scaffold(
    bottomBar = { Navbar(onClick = {navbarSelected = it}, value = navbarSelected)}
  ) {
    Column(modifier = Modifier.padding(it)) {
      when(navbarSelected){
        ctx.getString(R.string.home) -> HomeScreen()
        ctx.getString(R.string.stats) -> StatsScreen()
        ctx.getString(R.string.settings) -> SettingsScreen()
      }
    }
  }
}

