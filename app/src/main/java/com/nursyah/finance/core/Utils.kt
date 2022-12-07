package com.nursyah.finance.core

import android.app.Activity
import android.content.Context
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import com.nursyah.finance.core.Utils.Companion.SHARED_PREFERENCES
import com.nursyah.finance.core.Utils.Companion.USER_PREFERENCES

class Utils {
  companion object {
    const val SHARED_PREFERENCES = "sharedPreferences"
    const val USER_PREFERENCES = "userPreferences"

    val enterAnimated =  fadeIn()
    val exitAnimated = ExitTransition.None
  }
}

class User(app: Activity){
  private var sharedPreferences = app.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

  fun setUser(user: String) =
    sharedPreferences.edit()
      .putString(USER_PREFERENCES, user)
      .apply()

  fun getUser():String =
    sharedPreferences.getString(USER_PREFERENCES, "null").toString()
}
