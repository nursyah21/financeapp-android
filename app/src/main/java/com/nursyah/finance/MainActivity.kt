package com.nursyah.finance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nursyah.finance.ui.FinanceApp
import com.nursyah.finance.ui.theme.FinanceTheme

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
fun App(){
  Surface(modifier = Modifier.fillMaxSize()) {
    CardSummary()
  }
}

@Composable
fun CardSummary(){
  Card {
    Text("test")
  }
}