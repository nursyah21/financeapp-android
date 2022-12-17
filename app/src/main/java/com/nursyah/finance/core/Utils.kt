package com.nursyah.finance.core

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.text.isDigitsOnly
import com.nursyah.finance.R
import com.nursyah.finance.db.model.Data
import com.nursyah.finance.presentation.MainActivity
import java.text.SimpleDateFormat
import java.util.*

object Utils {
  const val TIME_WITH_HOUR = "ssmmHH_ddMMyyyy"
  const val TIME_TEXT_MONTH = "dd-MMMM-yyyy"
  @SuppressLint("SimpleDateFormat")
  fun getDateToday(pattern: String): String {
    return SimpleDateFormat(pattern).format(Calendar.getInstance().time)
  }

  // category: Spending | Income
  fun totalData(data: List<Data>, category: String):Long {
    var res:Long = 0
    data.filter { it.category == category }.forEach {
      res += it.value
    }
    return res
  }

  fun totalDataString(data: List<Data>, category: String):String {
    if(data.isEmpty()) return "0"

    var res:Long = 0
    data.filter { it.category == category }.forEach {
      res += it.value
    }
    return convertText(res.toString())
  }

  private fun totalIncomeSpend(data: List<Data>):Long{
    val spending = totalData(data, "Spending")
    val income = totalData(data, "Income")
    return income-spending
  }

  fun totalBalance(data: List<Data>):Long{
    val spending = totalData(data, "balanceSpending")
    val income = totalData(data, "balanceIncome")
    return totalIncomeSpend(data) + (income-spending)
  }

  fun convertToLong(text: String):Long {
    return try {
      if(text.isDigitsOnly()) text.toLong() else 0
    } catch (_:Exception){ 0 }
  }

  fun convertText(text: String):String {
    if(text == "") return "0"
    val prefix = if(text.contains("-")) "-" else ""
    try {
      text.toLong()
    }catch (_:Exception){ return "0" }

    var res = ""
    text.filterNot { it == '-' }.reversed().forEachIndexed { index, c ->
      if(index % 3 == 0)res += ","
      res += c
    }
    return prefix + res.reversed().dropLast(1)
  }


  @SuppressLint("UnspecifiedImmutableFlag")
  fun notification(context:Context, description: String){
    val channelID = "notification"
    val title = "finance"
    val priority = NotificationCompat.PRIORITY_DEFAULT

    val intent = Intent(context, MainActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    } else {
      PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }


    val builder = NotificationCompat.Builder(context, channelID)
      .setSmallIcon(R.drawable.finance)
      .setContentTitle(title)
      .setContentText(description)
      .setStyle(
        NotificationCompat.BigTextStyle()
          .bigText(description)
      )
      .setPriority(priority)
      .setContentIntent(pendingIntent)
      .setAutoCancel(true)

    val notificationManager: NotificationManager =
      context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val importance = NotificationManager.IMPORTANCE_DEFAULT
      notificationManager.createNotificationChannel(
        NotificationChannel(channelID, title, importance)
      )
    }

    notificationManager.notify(0, builder.build())
  }

  fun showToast(context:Context, text: String){
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
  }
}