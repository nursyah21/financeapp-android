package com.nursyah.finance.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nursyah.finance.db.model.Data
import com.nursyah.finance.db.model.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate.now
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainViewModel @Inject constructor(
  private val dataRepository: DataRepository,
  @ApplicationContext private val context: Context
): ViewModel(){

  var data by mutableStateOf(Data(category = "", item = "", value = 0))
    private set
  val allData = dataRepository.getAllData()

  fun addData(data: Data) = viewModelScope.launch(Dispatchers.IO) {
    dataRepository.addData(data)
  }

  fun getDataToday(): Flow<List<Data>> =
    dataRepository.getDataByDate(now().toString())

  fun deleteDataById(id: Long) = viewModelScope.launch(Dispatchers.IO) {
    dataRepository.deleteById(id)
  }

  fun deleteAllData() = viewModelScope.launch(Dispatchers.IO) {
    dataRepository.deleteAllData()
  }


  fun shareText(text: String){
    val intent = Intent().apply {
      action = Intent.ACTION_SEND
      putExtra(Intent.EXTRA_TEXT, text)
      type = "text/*"
    }
    context.startActivity(
      Intent.createChooser(intent, null).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
  }

  fun openLink(url: String){
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
  }

}