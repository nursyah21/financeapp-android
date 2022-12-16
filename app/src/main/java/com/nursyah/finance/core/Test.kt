package com.nursyah.finance.core

import android.app.Activity
import android.content.Context

class Test(private val context: Context) {
  fun getUser(): String =
    context.getSharedPreferences(Utils.SHARED_PREFERENCES, Context.MODE_PRIVATE)
      .getString(Utils.USER_PREFERENCES, "") ?: ""

}