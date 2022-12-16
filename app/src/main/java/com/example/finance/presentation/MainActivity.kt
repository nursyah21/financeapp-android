package com.example.finance.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.finance.presentation.components.Navbar
import com.example.finance.presentation.screens.HomeScreen
import com.example.finance.presentation.screens.SettingsScreen
import com.example.finance.presentation.screens.StatsScreen
import com.example.finance.presentation.theme.FinanceTheme
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
  var navbarSelected by remember { mutableStateOf("settings") }
  Scaffold(
    bottomBar = { Navbar(onClick = {navbarSelected = it}, value = navbarSelected)}
  ) {
    Column(modifier = Modifier.padding(it)) {
      when(navbarSelected){
        "home" -> HomeScreen()
        "stats" -> StatsScreen()
        "settings" -> SettingsScreen()
      }
    }
  }
}

