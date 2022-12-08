package com.nursyah.finance.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
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

    fun openLink(app: Context, url: String){
      val intent = Intent(Intent.ACTION_VIEW,Uri.parse(url))
      app.startActivity(intent)
    }

    fun shareText(app: Context, text: String){
      val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "plain/text"
      }
      app.startActivity(Intent.createChooser(intent, null))
    }
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
