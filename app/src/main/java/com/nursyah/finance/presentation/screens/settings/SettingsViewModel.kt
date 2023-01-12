package com.nursyah.finance.presentation.screens.settings

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.*
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nursyah.finance.R
import com.nursyah.finance.core.Constants.SETTINGS_STATE_BACKUP
import com.nursyah.finance.core.Constants.SETTINGS_STATE_DELETE
import com.nursyah.finance.core.Constants.SETTINGS_STATE_RESTORE
import com.nursyah.finance.core.Constants.TIME_WITH_HOUR
import com.nursyah.finance.core.Utils
import com.nursyah.finance.core.Utils.getDateToday
import com.nursyah.finance.db.model.Data
import com.nursyah.finance.db.model.DataRepository
import com.nursyah.finance.presentation.components.MainViewModel
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
  var statusBackupRestore by mutableStateOf("")
    private set
  private val mainViewModel = MainViewModel(dataRepository, context)
  fun deleteAlert(){
    stateBackupRestore = SETTINGS_STATE_DELETE
    changeAlertDialog()
  }
  fun restoreAlert(){
    stateBackupRestore = SETTINGS_STATE_RESTORE
    changeAlertDialog()
  }
  fun backupAlert(){
    stateBackupRestore = SETTINGS_STATE_BACKUP
    changeAlertDialog()
  }
  fun deleteData() {
    mainViewModel.deleteAllData()
    statusBackupRestore = context.getString(R.string.delete_data_success)
    Utils.showToast(context, context.getString(R.string.delete_data_success))
    Utils.notification(context, context.getString(R.string.delete_data_success))
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

    val external = Environment.getExternalStoragePublicDirectory(
      Environment.DIRECTORY_DOCUMENTS
    )

    val nameFile = "backup finance ${getDateToday(TIME_WITH_HOUR)
      .replace("_"," at ")}.csv"
    val status = "${context.getString(R.string.backup_data_success)}\n${external.path}/$nameFile"

    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val backupData = ContentValues().apply {
          put(MediaStore.MediaColumns.DISPLAY_NAME, nameFile)
          put(MediaStore.MediaColumns.MIME_TYPE, "*/*")
          put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
        }

        val uri = try{
          context.contentResolver.insert(
            MediaStore.Files.getContentUri("external"),
            backupData
          )
        }catch (_:Exception){
          context.contentResolver.insert(
            MediaStore.Files.getContentUri("internal"),
            backupData
          )
        }
        with(context.contentResolver.openOutputStream(uri!!)) {
          this?.write(text.toByteArray())
        }
      }
      else{
        val file = File(external, nameFile)
        file.setWritable(true)
        file.writeText(text)
      }
      Utils.showToast(context, status)
      Utils.notification(context, status, file = external)
      statusBackupRestore = status
    }catch (e:Exception){
      Utils.showToast(context, context.getString(R.string.backup_data_failed))
      println(e)
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

      Utils.notification(context,context.getString(R.string.restore_data_success))
      Utils.showToast(context,context.getString(R.string.restore_data_success))
      statusBackupRestore = context.getString(R.string.restore_data_success)
    }catch (_:Exception){
      Utils.showToast(context,context.getString(R.string.restore_data_failed))
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
        Utils.showToast(context,context.getString(R.string.data_not_valid))
      }
      return textList.drop(1)
    }catch (e:Exception){
      Utils.showToast(context,context.getString(R.string.restore_data_failed))
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