package com.nursyah.finance.presentation.screens.stats

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nursyah.finance.R
import com.nursyah.finance.core.Utils
import com.nursyah.finance.db.model.Data
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class StatsViewModel @Inject constructor(
  @ApplicationContext private val context: Context
): ViewModel() {

  var stateDataAlert by mutableStateOf(false)
    private set
  var stateDataStatus by mutableStateOf("")
    private set
  var stateDataId by mutableStateOf<Long>(0)
    private set
  fun changeStateAlert(){ stateDataAlert = !stateDataAlert }
  fun changeDataStatus(text: String){ stateDataStatus = text }
  fun changeDataId(id: Long){stateDataId = id}

  object Category {
    const val INCOME = "Income"
    const val SPENDING = "Spending"
  }

  fun sortedData(data: List<Data>):List<Data>{
    if(data.isEmpty())return emptyList()
    try {
      val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
       return data.sortedByDescending { LocalDate.parse(it.date, dateFormatter) }
    }catch (_:Exception){ 
      Utils.showToast(context, context.getString(R.string.failed_to_parse_data))
    }
    return emptyList()
  }

  fun accData(_data: List<Data>, category: String):List<Data> {
    val data = _data.filter { it.category == category }
    val setDate = mutableSetOf<String>()
    data.forEach { setDate.add(it.date) }

    val res = mutableListOf<Data>()
    setDate.forEach {date ->
      val value = data.filter { it.date == date }.reduce { acc, data ->
        Data(date = date, category = category, value = acc.value + data.value)
      }
      res.add(value)
    }

    return res.reversed()
  }

}