package com.nursyah.finance.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import com.nursyah.finance.core.Utils.Companion.DATA_PREFERENCES
import com.nursyah.finance.core.Utils.Companion.ITEM_PREFENCES
import com.nursyah.finance.core.Utils.Companion.SHARED_PREFERENCES
import com.nursyah.finance.core.Utils.Companion.USER_PREFERENCES
import com.nursyah.finance.core.Utils.Companion.VALUE_PREFERENCES
import com.nursyah.finance.model.Time
import java.util.*
import kotlin.collections.HashMap

class Utils {
  companion object {
    //preerences for email
    const val SHARED_PREFERENCES = "sharedPreferences"
    const val USER_PREFERENCES = "userPreferences"

    //preferences for item and value
    const val DATA_PREFERENCES = "dataPreferences"
    const val ITEM_PREFENCES = "itemPreferences"
    const val VALUE_PREFERENCES = "valuePreferences"

    val enterAnimatedFade =  fadeIn()
    val exitAnimatedFade = fadeOut()
    val enterAnimatedSlideVertical = slideInVertically(initialOffsetY = {it})


    fun openLink(app: Context, url: String){
      val intent = Intent(Intent.ACTION_VIEW,Uri.parse(url))
      app.startActivity(intent)
    }

    fun shareText(app: Context, text: String){
      val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/*"
      }
      app.startActivity(Intent.createChooser(intent, null))
    }
    fun getUserEmail(email:String):String {
      var idx = 0
      for(i in 0..email.length){
        if(email[i] == '@') break else idx++
      }

      return email.substring(0, idx)
    }

    fun getTime(): Time{
      val calendar = Calendar.getInstance()
      return Time(
        Date = calendar.get(Calendar.DATE),
        Month = calendar.get(Calendar.MONTH),
        Year = calendar.get(Calendar.YEAR)
      )
    }


    fun formatMoney(_value: String): String{
      val value = if (_value == "") 0 else _value

      var res = ""
      value.toString().reversed().forEachIndexed{idx, ch ->
        if(idx % 3 == 0)res = res.plus(',')
        res = res.plus(ch)
      }
      return  res.reversed().dropLast(1)
    }
  }
}

class User(app: Context){
  private var sharedPreferences = app.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

  fun setUser(user: String) =
    sharedPreferences.edit()
      .putString(USER_PREFERENCES, user)
      .apply()

  fun getUser():String =
    sharedPreferences.getString(USER_PREFERENCES, "null").toString()
}

class Data(app: Context){
  private var dataPreferences = app.getSharedPreferences(DATA_PREFERENCES, Context.MODE_PRIVATE)
  fun setData(item: String, value: String) =
    dataPreferences.edit()
      .putString(ITEM_PREFENCES, item)
      .putString(VALUE_PREFERENCES, value)
      .apply()

  fun getData(data: MutableMap<String, *>? = dataPreferences.all): HashMap<String, String> =
    hashMapOf(
      "item" to data!![ITEM_PREFENCES].toString(),
      "value" to data[VALUE_PREFERENCES].toString()
    )
}
