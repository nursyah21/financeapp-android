package com.nursyah.finance.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun LoginScreen(
  signIn: () -> Unit
){
//  val user
  Surface(modifier = Modifier.fillMaxSize()) {
    Column(
     verticalArrangement = Arrangement.Center,
     horizontalAlignment = Alignment.CenterHorizontally
    ) {
      TextButton(onClick = signIn) {
        Text(text = "Continue with google")
      }
    }
  }

}