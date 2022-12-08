package com.nursyah.finance.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.nursyah.finance.R
import com.nursyah.finance.core.User

enum class ItemNav(
  val itemName: String,
  val itemIcon: Int
){
  HOME(itemName = "home", itemIcon = R.drawable.ic_dashboard),
  STATS(itemName = "stats", itemIcon = R.drawable.ic_stats),
  ACCOUNT(itemName = "account", itemIcon = R.drawable.ic_account)
}

class MainViewModel: ViewModel() {
  val currentUser:MutableState<String> = mutableStateOf("")
  val selectedNavItem:MutableState<String> = mutableStateOf("account")

  val signInIntent = AuthUI.getInstance()
    .createSignInIntentBuilder().setAvailableProviders(
      arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build()
      )
    ).build()

  @SuppressLint("RestrictedApi")
  fun onSignInResult(result: FirebaseAuthUIAuthenticationResult, user: User){
    val resp = result.idpResponse
    if(result.resultCode == RESULT_OK){
      currentUser.value = FirebaseAuth.getInstance().currentUser?.email.toString()
      user.setUser(currentUser.value)
      Log.d("TAG","success")

    }else{
      Log.d("TAG", "failed $resp")
    }
  }

  fun signOut(app: Context){
    val user = User(app as Activity)
    AuthUI.getInstance().signOut(app).addOnSuccessListener {
      user.setUser("null")
      currentUser.value = "null"
      Log.d("TAG", "sign out")
    }
  }


}