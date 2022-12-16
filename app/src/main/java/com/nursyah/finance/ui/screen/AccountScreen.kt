package com.nursyah.finance.ui.screen

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nursyah.finance.BuildConfig
import com.nursyah.finance.R
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
  var confirmSignOut by remember { mutableStateOf(false) }


  AnimatedVisibility(
    visible = enabled,
    modifier = Modifier.padding(padding),
    enter = Utils.enterAnimatedFade,
    exit = Utils.exitAnimatedFade
  ) {
    Surface(
      modifier = Modifier.fillMaxSize()
    ) {
      //user login
      
      Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(5.dp)
      ) {
        var userEmail = Utils.getUserEmail(email)
        userEmail = if(userEmail.length < 20) userEmail else userEmail.dropLast(3).plus("...")
        Text(
          text = "user: $userEmail",
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        )
        TextButton(
          onClick = {confirmSignOut = !confirmSignOut},
          border = ButtonDefaults.outlinedBorder
        ) {
          Text(text = "Sign Out")
        }
      }

      About()
      
      Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(text = "version: ${BuildConfig.VERSION_NAME}")
        Spacer(modifier = Modifier.height(10.dp))
      }
    }
    
    if(confirmSignOut) ConfirmSignOut(
      onDismiss = {confirmSignOut = !confirmSignOut},
      confirmButton = signOut
    )
  }
}

@Composable
fun About() {
  val context = LocalContext.current
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = stringResource(R.string.about),
      textAlign = TextAlign.Center,
      modifier = Modifier.width(200.dp)
    )
    Spacer(modifier = Modifier.height(30.dp))
    
    Text("Want to Support?")
    //github
    TextButton(
      onClick = { Utils.openLink(context, "https://github.com/nursyah21/financeapp-android") },
      border = ButtonDefaults.outlinedBorder
    ) {
      Text(text = "Contribute with github")
    }
    //share
    TextButton(
      onClick = { Utils.shareText(context, context.getString(R.string.share_app)) },
      border = ButtonDefaults.outlinedBorder
    ) {
      Text(text = "Share with Friends")
    }
    //support with ko-fi
    TextButton(
      onClick = { Utils.openLink(context, context.getString(R.string.ko_fi)) },
      border = ButtonDefaults.outlinedBorder
    ) {
      Text(text = "Support with ko-fi")
    }
    //support with trakteer
    TextButton(
      onClick = { Utils.openLink(context, context.getString(R.string.trakteer)) },
      border = ButtonDefaults.outlinedBorder
    ) {
      Text(text = "Support with trakteer")
    }
  }
}


@Composable
fun ConfirmSignOut(
  onDismiss: () -> Unit = {},
  confirmButton: () -> Unit = {}
){
  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      TextButton(
        onClick = confirmButton,
        border = ButtonDefaults.outlinedBorder
      ) {
        Text(text = "Yes")
      }
    },
    title = {
      Text(
        text = "Do you want to sign out",
      )
    }
  )
}