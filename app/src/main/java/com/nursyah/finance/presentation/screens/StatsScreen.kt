package com.nursyah.finance.presentation.screens

import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.nursyah.finance.core.Utils
import com.nursyah.finance.db.model.Data
import com.nursyah.finance.presentation.screens.StatsViewModel.Category.INCOME
import com.nursyah.finance.presentation.screens.StatsViewModel.Category.SPENDING
import com.nursyah.finance.presentation.theme.cardModifier
import com.nursyah.finance.presentation.theme.modifierScreen


@Composable
fun StatsScreen() {
  val statsViewModel = StatsViewModel()
  val data by remember { mutableStateOf(statsViewModel.populateData()) }

  Surface(modifierScreen) {
    Column(modifierScreen.fillMaxWidth().zIndex(1f),
      horizontalAlignment = Alignment.End) {
      OutlinedButton(onClick = { statsViewModel.changeStateChart() }) {
        Text(text = statsViewModel.stateChart)
      }
    }

    Card(cardModifier.height(235.dp).zIndex(0f)) {
      ChartData(statsViewModel, data)
    }
  }

}

@Composable
private fun ChartData(
  viewModel: StatsViewModel, data: List<Data>
) {
  val income = viewModel.accData(data, INCOME)
  val spending = viewModel.accData(data, SPENDING)

  val maxIncome = income.maxBy { it.value }.value
  val maxSpending = spending.maxBy { it.value }.value

  val scrollState = rememberScrollState()

  Row(
    Modifier
      .padding(5.dp)
      .horizontalScroll(scrollState)) {
    spending.forEach{
      Column(
        Modifier
          .padding(10.dp)
          .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
      ) {
        //val height = (floor(it.value.toDouble() / maxSpending) *120).dp
        val height = (it.value.toFloat()/maxSpending*120).toInt().dp
        println("${it.value}, $maxSpending ${(it.value.toFloat()/maxSpending*120).toInt()}")

        Box(
          Modifier
            .size(30.dp, height)
            .background(Color.White.copy(alpha = .7f)))
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

  LaunchedEffect(Unit){
    scrollState.animateScrollTo(Int.MAX_VALUE, spring())
  }

}
