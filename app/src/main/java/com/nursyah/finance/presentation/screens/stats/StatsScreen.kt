package com.nursyah.finance.presentation.screens.stats

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nursyah.finance.R
import com.nursyah.finance.core.Utils
import com.nursyah.finance.db.model.Data
import com.nursyah.finance.presentation.components.AlertComponent
import com.nursyah.finance.presentation.components.MainViewModel
import com.nursyah.finance.presentation.screens.stats.StatsViewModel.Category.ALL_TIME
import com.nursyah.finance.presentation.screens.stats.StatsViewModel.Category.INCOME
import com.nursyah.finance.presentation.screens.stats.StatsViewModel.Category.MONTH
import com.nursyah.finance.presentation.screens.stats.StatsViewModel.Category.SPENDING
import com.nursyah.finance.presentation.screens.stats.StatsViewModel.Category.WEEK
import com.nursyah.finance.presentation.screens.stats.StatsViewModel.Category.YEAR
import com.nursyah.finance.presentation.screens.stats.StatsViewModel.Category.stateSummary
import com.nursyah.finance.presentation.theme.cardModifier
import com.nursyah.finance.presentation.theme.modifierScreen


@Composable
fun StatsScreen(
  mainViewModel: MainViewModel = hiltViewModel(),
  viewModel: StatsViewModel = hiltViewModel(),
  navHostController: NavHostController,
) {
  val unsortedData by mainViewModel.allData.collectAsState(emptyList())
  val data = viewModel.sortedData(unsortedData)
  val scrollState = rememberScrollState()

  val income = viewModel.accData(data, INCOME)
  val spending = viewModel.accData(data, SPENDING)
  val errorData = viewModel.validData


  Column(
    modifierScreen.verticalScroll(scrollState),
    verticalArrangement = Arrangement.spacedBy(15.dp)
  ) {
    Chart(spending, income, viewModel, navHostController)
    Summary(data)
    Divider()
    Text(text = stringResource(R.string.history))
    if(errorData.isNotBlank())Text(text = errorData)
    DataColumn(data, viewModel)
  }
}

@Composable
private fun Summary(data: List<Data>) {
  val ctx = LocalContext.current

  //filter based month and year
  val setData = mutableSetOf<String>()
  val listData = mutableListOf<SummaryData>()

  data.reversed().forEach {
    setData.add(it.date.dropLast(3))
  }

  try {
    setData.forEach {
      val text = Utils.convertDateString(ctx, it)
      var accSpending = 0L
      var accIncome = 0L
      data.reversed().filter { itData -> itData.date.contains("$it-.*".toRegex()) }.forEach {itAcc->
        if(itAcc.category == "Spending")accSpending += itAcc.value
        if(itAcc.category == "Income") accIncome += itAcc.value
      }
      listData.add(
        SummaryData(text, Utils.convertText(accIncome.toString()), Utils.convertText(accSpending.toString()))
      )
    }
  }catch (e:Exception){
    println("error: $e")
  }

  val scrollState = rememberScrollState()
  Row(
    Modifier
      .padding(horizontal = 8.dp)
      .horizontalScroll(scrollState),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    listData.forEach {
      Card(Modifier.clip(RoundedCornerShape(10.dp))) {
        Column(
          Modifier
            .padding(8.dp)
            .width(180.dp)) {
          Text(
            text = it.text,
            modifier = Modifier.padding(horizontal = 5.dp),
          )
          Divider(
            Modifier
              .width(180.dp)
              .padding(vertical = 4.dp))
          //box
          val scrollStateBox = rememberScrollState()
          Row(
            Modifier.horizontalScroll(scrollStateBox),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Column(Modifier.padding(8.dp)) {
              Text(text = ctx.getString(R.string.spending))
              Text(it.spending)
            }
            Column(Modifier.padding(8.dp)) {
              Text(text = ctx.getString(R.string.income))
              Text(it.income)
            }
          }
        }
      }
    }
  }

  LaunchedEffect(Unit){
    scrollState.animateScrollTo(Int.MAX_VALUE, spring())
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
        val category =
          if(it.category == "Spending") stringResource(R.string.spending)
          else stringResource(R.string.income)
        val value = "${Utils.convertDate(it.date)}: ${Utils.convertText(it.value.toString())} ($category)"
        
        Column(
          Modifier
            .fillMaxWidth()
            .clickable {
              viewModel.changeStateAlert()
              viewModel.changeDataStatus(value)
              viewModel.changeDataId(it.id)
            }) {
          Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = value, color = Color.White.copy(alpha = .7f), fontSize = 14.sp)
            Icon(
              painter = painterResource(R.drawable.ic_delete),
              contentDescription = null,
              tint = Color.DarkGray
            )
          }
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
        Text(text = stringResource(R.string.yes), textDecoration = TextDecoration.Underline)
      }
    }
  ){
    Text(text = "${stringResource(R.string.are_you_sure_to_delete)}\n${viewModel.stateDataStatus}")
  }
}

@Composable
private fun Chart(spending: List<Data>, income: List<Data>, viewModel: StatsViewModel, navHostController: NavHostController) {
  //Spending Chart
  val heightIncome = if(income.isEmpty()) 100.dp else 235.dp
  val heightSpend = if(spending.isEmpty()) 100.dp else 235.dp

  val filterSpending = when(viewModel.chartSpending){
    ALL_TIME -> spending
    YEAR -> spending.filter { it.date.contains("${Utils.getThisYear()}-.*-.*".toRegex()) }
    MONTH -> spending.filter { it.date.contains("${Utils.getThisYear()}-${Utils.getThisMonth()}-.*".toRegex()) }
    WEEK -> {
      if(spending.size > 7){
        spending.subList(spending.size - 7, spending.size).filter { it.date.contains("${Utils.getThisYear()}-${Utils.getThisMonth()}-.*".toRegex()) }
      }else spending
    }
    else -> spending
  }

  val filterIncome = when(viewModel.chartIncome){
    ALL_TIME -> income
    YEAR -> income.filter { it.date.contains("${Utils.getThisYear()}-.*-.*".toRegex()) }
    MONTH -> income.filter { it.date.contains("${Utils.getThisYear()}-${Utils.getThisMonth()}-.*".toRegex()) }
    WEEK -> {
      if(income.size > 7){
        income.subList(income.size - 7, income.size).filter { it.date.contains("${Utils.getThisYear()}-${Utils.getThisMonth()}-.*".toRegex()) }
      }else income
    }
    else -> income
  }

  //Spending Chart
  ChartContainer(heightSpend, viewModel, filterSpending, SPENDING, navHostController)

  //Income Chart
  ChartContainer(heightIncome, viewModel, filterIncome, INCOME, navHostController)
}

@Composable
private fun ChartContainer(
  heightSpend: Dp, viewModel: StatsViewModel, data: List<Data>, s: String, navHostController: NavHostController
) {
  Card(
    cardModifier
      .height(heightSpend)
      .animateContentSize()
      .zIndex(0f)
  ) {

    Column {
      Row {
        Text(
          text = if(s == SPENDING) stringResource(R.string.spending) else stringResource(R.string.income),
          fontSize = MaterialTheme.typography.headlineSmall.fontSize,
          modifier = Modifier.padding(8.dp)
        )
        StateChart(s, viewModel, navHostController)
      }

      Divider()
      Surface {
        ChartData(data)
      }
    }
  }
}

@Composable
private fun StateChart(
  s: String,
  viewModel: StatsViewModel,
  navHostController: NavHostController
) {
  val ctx = LocalContext.current

//  viewModel.chartSpending = Utils.getSharedString(ctx, Utils.SHARED_CHART_SPENDING, ALL_TIME)
//  viewModel.chartIncome = Utils.getSharedString(ctx, Utils.SHARED_CHART_INCOME, ALL_TIME)


  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    items(stateSummary) {
      var text = ""
      when (it) {
        ALL_TIME -> {
          text = stringResource(id = R.string.all_time)
        }
        YEAR -> {
          text = stringResource(id = R.string.year)
        }
        MONTH -> {
          text = stringResource(id = R.string.month)
        }
        WEEK -> {
          text = stringResource(id = R.string.week)
        }
      }

      //refresh
      when(s){
        SPENDING ->
          ChartStateButton(text = text, active = (it == viewModel.chartSpending)) {
            viewModel.chartSpending = it
            Utils.saveSharedString(ctx, it, Utils.SHARED_CHART_SPENDING)
            navHostController.popBackStack()
            navHostController.navigate(ctx.getString(R.string.stats))
          }
        INCOME ->
          ChartStateButton(text = text, active = (it == viewModel.chartIncome)) {
            viewModel.chartIncome = it
            Utils.saveSharedString(ctx, it, Utils.SHARED_CHART_INCOME)
            navHostController.popBackStack()
            navHostController.navigate(ctx.getString(R.string.stats))
          }
      }
    }
  }
}

@Composable
fun ChartStateButton(
  text: String,
  active: Boolean,
  onClick: () -> Unit
) {
  OutlinedButton(onClick = onClick) {
    Text(
      text = text,
      textDecoration = if(active) TextDecoration.Underline else TextDecoration.None
    )
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
          text = Utils.convertDate(it.date),
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
