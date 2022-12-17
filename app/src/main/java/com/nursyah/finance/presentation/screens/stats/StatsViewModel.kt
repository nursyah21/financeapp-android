package com.nursyah.finance.presentation.screens.stats

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nursyah.finance.db.model.Data
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StatsViewModel: ViewModel() {

  var stateDataAlert by mutableStateOf(false)
    private set
  var stateDataStatus by mutableStateOf("")
    private set
  fun changeDataAlert(){ stateDataAlert = !stateDataAlert }
  fun changeDataStatus(text: String){ stateDataStatus = text }

  object Category {
    const val INCOME = "Income"
    const val SPENDING = "Spending"
  }

  fun populateData(): List<Data>{
    val data = mutableListOf<Data>()
    val date = (1..3).map { "%02d".format(it) }
    val month = (1..4).map { "%02d".format(it) }
    val year = (21..22).map { "20$it" }
    val category = "Spending,Income".split(",")
    val value = "10,15,22,13,20".split(",").map { "${it}000".toLong() }
    repeat(100){
      val time = "${date.random()}-${month.random()}-${year.random()}"
      data.add(Data(date = time, category = category.random(), value = value.random()))
    }
    return sortedData(data)
  }

  private fun sortedData(data: List<Data>):List<Data>{
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return data.sortedByDescending { LocalDate.parse(it.date, dateFormatter) }
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