package com.nursyah.finance.presentation.screens.stats

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.nursyah.finance.core.Utils
import com.nursyah.finance.db.model.Data
import com.nursyah.finance.presentation.components.AlertComponent
import com.nursyah.finance.presentation.components.MainViewModel
import com.nursyah.finance.presentation.screens.stats.StatsViewModel.Category.INCOME
import com.nursyah.finance.presentation.screens.stats.StatsViewModel.Category.SPENDING
import com.nursyah.finance.presentation.theme.cardModifier
import com.nursyah.finance.presentation.theme.modifierScreen


@Composable
fun StatsScreen(
  mainViewModel: MainViewModel = hiltViewModel(),
) {
  val viewModel = StatsViewModel()
  val data by mainViewModel.allData.collectAsState(emptyList())
  val scrollState = rememberScrollState()

  val income = viewModel.accData(data, INCOME)
  val spending = viewModel.accData(data, SPENDING)

  Column(
    modifierScreen.verticalScroll(scrollState),
    verticalArrangement = Arrangement.spacedBy(15.dp)
  ) {
    Chart(spending, income)
    Divider()
    Text(text = "History")
    DataColumn(data, viewModel)
  }
}

@Composable
private fun DataColumn(
  data: List<Data>,
  viewModel: StatsViewModel,
  mainViewModel: MainViewModel = hiltViewModel(),
) {
  Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
    data.filterNot { it.category == "balanceSpending" || it.category == "balanceIncome" }
      .forEach {
        val value = "${it.date}: ${Utils.convertText(it.value.toString())} (${it.category})"
        
        Column(
          Modifier
            .fillMaxWidth()
            .clickable {
              viewModel.changeStateAlert()
              viewModel.changeDataStatus(value)
              viewModel.changeDataId(it.id)
            }) {
          Text(text = value, color = Color.White.copy(alpha = .7f), fontSize = 14.sp)
        }
        Divider()
    }
  }

  AlertComponent(
    visible = viewModel.stateDataAlert,
    onDismiss = { viewModel.changeStateAlert() },
    confirmButton = {
      TextButton(onClick = {
        mainViewModel.deleteDataById(viewModel.stateDataId)
        viewModel.changeStateAlert()
      }) {
        Text(text = "Yes", textDecoration = TextDecoration.Underline)
      }
    },
    title = "Delete Data"
  ){
    Text(text = "Are you sure to delete\n${viewModel.stateDataStatus}")
  }
}


@Composable
private fun Chart(spending: List<Data>, income: List<Data>) {
  //Spending Chart
  val heightIncome = if(income.isEmpty()) 100.dp else 235.dp
  val heightSpend = if(spending.isEmpty()) 100.dp else 235.dp
  Card(
    cardModifier
      .height(heightSpend).animateContentSize()
      .zIndex(0f)
  ) {
    Column {
      Text(
        text = "Spending",
        fontSize = MaterialTheme.typography.h6.fontSize,
        modifier = Modifier.padding(8.dp)
      )
      Divider()
      Surface {
        ChartData(spending)
      }
    }
  }

  //Income Chart
  Card(
    cardModifier
      .height(heightIncome).animateContentSize()
      .zIndex(0f)
  ) {
    Column {
      Text(
        text = "Income",
        fontSize = MaterialTheme.typography.h6.fontSize,
        modifier = Modifier.padding(8.dp)
      )
      Divider()
      Surface {
        ChartData(income)
      }
    }
  }
}

@Composable
private fun ChartData(
  data: List<Data>
) {
  val scrollState = rememberScrollState()
  val maxData = if(data.isEmpty()) 0L else data.maxBy { it.value }.value
  val minData = if(data.isEmpty()) 0L else data.minBy { it.value }.value
  val avgData = if(data.isEmpty()) 0L else data.reduce {
      acc, d -> Data(date="", category = "", value = acc.value + d.value) }.value
  val avgDataValue = if(data.isEmpty()) "0" else (avgData.toFloat() / data.size).toInt().toString()

  Row(
    Modifier
      .padding(5.dp)
      .horizontalScroll(scrollState)
  ) {
    data.forEach {
      Column(
        Modifier
          .padding(horizontal = 10.dp, vertical = 5.dp)
          .padding(bottom = 20.dp)
          .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
      ) {
        val height = (it.value.toFloat() / maxData * 110).toInt().dp

        Box(
          Modifier
            .size(30.dp, height)
            .background(Color.White.copy(alpha = .7f))
        )
        Spacer(Modifier.height(5.dp))
        Text(
          text = Utils.convertText(it.value.toString()),
          fontSize = 12.sp,
          color = Color.White.copy(alpha = .7f)
        )
        Text(
          text = it.date,
          fontSize = 10.sp,
          color = Color.White.copy(alpha = .7f)
        )
      }
    }
  }

  val scrollStateInfo = rememberScrollState()

  Column(
    Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Bottom
  ) {
    Divider()
    Row(Modifier.horizontalScroll(scrollStateInfo)) {
      Text("max: ${Utils.convertText(maxData.toString())} | " +
          "min: ${Utils.convertText(minData.toString())} | " +
          "avg: ${Utils.convertText(avgDataValue)}",
        fontSize = 14.sp,
        modifier = Modifier.padding(5.dp)
      )
    }

  }

  LaunchedEffect(Unit){
    scrollState.animateScrollTo(Int.MAX_VALUE, spring())
  }

}
