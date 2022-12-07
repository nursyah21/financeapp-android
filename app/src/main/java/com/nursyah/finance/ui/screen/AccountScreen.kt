package com.nursyah.finance.ui.screen

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nursyah.finance.core.User
import com.nursyah.finance.core.Utils

@Composable
fun AccountScreen(
  enabled: Boolean,
  padding: PaddingValues,
  signOut: () -> Unit
){
  val context = LocalContext.current
  val user = User(context as Activity)
  val email = user.getUser()

  AnimatedVisibility(
    visible = enabled,
    modifier = Modifier.padding(padding),
    enter = Utils.enterAnimated,
    exit = Utils.exitAnimated
  ) {
    Surface(
      modifier = Modifier.fillMaxSize()
    ) {
      Text(
        text = "email: $email",
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
      )
      Column( 
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        TextButton(onClick = signOut) {
          Text(text = "Sign Out")
        }
      }
    }
  }
}