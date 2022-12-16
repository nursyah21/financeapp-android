package com.example.finance.presentation.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.compose.runtime.*
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance.core.Utils
import com.example.finance.db.model.Data
import com.example.finance.db.model.DataRepository
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.util.*
import javax.inject.Inject


@SuppressLint("StaticFieldLeak")
@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val dataRepository: DataRepository,
  @ApplicationContext private val context: Context
): ViewModel() {
  var alertDialog by mutableStateOf(false)
    private set
  fun changeAlertDialog() { alertDialog = !alertDialog }
  var stateBackupRestore by mutableStateOf("")
    private set
  fun deleteAlert(){
    stateBackupRestore = "delete"
    changeAlertDialog()
  }
  fun restoreAlert(){
    stateBackupRestore = "restore"
    changeAlertDialog()
  }
  fun backupAlert(){
    stateBackupRestore = "backup"
    changeAlertDialog()
  }

  fun activityOpenSource(){
    val intent = Intent(context, OssLicensesMenuActivity::class.java).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
  }

  fun backupData()= viewModelScope.launch{
    var text = "date,category,item,value\n"
    dataRepository.getAllData().collect{
      it.forEach {data->
        text += "${data.date},${data.category},${data.item},${data.value}\n"
      }
      sendData(text)
    }
  }
  @SuppressLint("SimpleDateFormat")
  private fun sendData(text: String){
    val external = Environment.getExternalStorageDirectory()
    var write = true
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
      write = false
      val intent = Intent(android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
      context.startActivity(intent)
    }

    val time = java.text.SimpleDateFormat("ssmmHH_ddMMyyyy")
      .format(Calendar.getInstance().time)
    try {
      if(!write)return Utils.showToast(context, "Please allow access to files permission")
      val file = File(external, "finance_$time.csv")
      file.setWritable(true)
      file.writeText(text)
      Utils.showToast(context, "Backup Data Success")
      Utils.notification(context, "Backup Data Success\n${file.path}")
    }catch (_:Exception){
      Utils.showToast(context, "Backup Data Failed")
    }
  }

  fun restoreData(path: Uri) {
    val data = getData(path)
    try {
      viewModelScope.launch(Dispatchers.IO) {
        data.forEach { c ->
          if (c.isNotEmpty()) {
            val newData = convertToData(c)
            dataRepository.addData(newData)
          }
        }
      }

      Utils.notification(context,"Restore Data Success")
      Utils.showToast(context,"Restore Data Success")
    }catch (_:Exception){
      Utils.showToast(context,"RestoreData Failed")
    }

  }
  private fun getData(path:Uri):List<String>{
    try {
      //get text from input stream
      var text = ""
      val file:InputStream = context.contentResolver.openInputStream(path)!!
      file.bufferedReader().use {
        text += it.readText()
      }
      file.close()

      //check data valid
      val textList = text.split("\n")
      if(textList[0] != "date,category,item,value"){
        Utils.showToast(context,"Data Not Valid")
      }
      return textList.drop(1)
    }catch (e:Exception){
      Utils.showToast(context,"Restore Data Failed")
      println("Error: ${e.stackTrace}")
    }
    return emptyList()
  }

  private fun convertToData(text: String): Data{
    val i=text.split(",")
    return if (i[3].isDigitsOnly()) {
      Data(date = i[0], category = i[1], item = i[2], value = i[3].toLong())
    } else
      Data(date = i[0], category = "balanceSpending", item = i[2], value = 0)
  }

}