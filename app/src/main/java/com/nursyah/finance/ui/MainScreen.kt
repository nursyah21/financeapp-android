package com.nursyah.finance.ui

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.nursyah.finance.core.User
import com.nursyah.finance.ui.components.BottomNavbar
import com.nursyah.finance.ui.screen.AccountScreen
import com.nursyah.finance.ui.screen.HomeScreen
import com.nursyah.finance.ui.screen.LoginScreen
import com.nursyah.finance.ui.screen.StatsScreen
import com.nursyah.finance.ui.theme.FinanceTheme

val mainViewModel = MainViewModel()

@Composable
fun FinanceApp(){
  FinanceTheme {
    val context = LocalContext.current
    val user = User(context as Activity)
    val currentUser = mainViewModel.currentUser
    currentUser.value = user.getUser()

    val launcher = rememberLauncherForActivityResult(
      contract = FirebaseAuthUIActivityResultContract()
    ){
      mainViewModel.onSignInResult(it, user)
    }

    if(currentUser.value == "null") LoginScreen(
      signIn = { launcher.launch(mainViewModel.signInIntent) }
    )
    else MainScreen()
  }
}

@Composable
fun MainScreen(){
  val context = LocalContext.current
  val selectedNav = mainViewModel.selectedNavItem

  Scaffold(
    bottomBar = {
      BottomNavbar(
        onClick = { selectedNav.value = it },
        value = selectedNav.value
      )
    }
  ) {
    HomeScreen(
      padding = it,
      enabled = "home" == selectedNav.value
    )
    StatsScreen(
      padding = it,
      enabled = "stats" == selectedNav.value
    )
    AccountScreen(
      padding = it,
      enabled = "account" == selectedNav.value,
      signOut = {
        mainViewModel.signOut(app = context)
      }
    )
  }
}











