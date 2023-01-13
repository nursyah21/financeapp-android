package com.nursyah.finance.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nursyah.finance.R
import com.nursyah.finance.core.Constants.TIME_TEXT_MONTH
import com.nursyah.finance.core.Utils
import com.nursyah.finance.db.model.Data
import com.nursyah.finance.presentation.components.AlertComponent
import com.nursyah.finance.presentation.components.MainViewModel
import com.nursyah.finance.presentation.theme.AlmostBlack
import com.nursyah.finance.presentation.theme.cardModifier
import com.nursyah.finance.presentation.theme.modifierScreen
import java.time.LocalDate


@Composable
fun HomeScreen(
  viewModel: MainViewModel = hiltViewModel(),
){
  val homeViewModel = HomeViewModel()

  val data by viewModel.allData.collectAsState(initial = emptyList())
  homeViewModel.changeBalance(data)

  Surface {
    Column(modifierScreen) {
      Summary(onClick = { homeViewModel.changeBackdropState() }, homeViewModel = homeViewModel)
      Spacer(modifier = Modifier.height(15.dp))
      TodaySummary()
      Spacer(modifier = Modifier.height(15.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
      ) {
        Text(
          text = Utils.getDateToday(TIME_TEXT_MONTH),
          modifier = Modifier.padding(horizontal = 5.dp))
      }

      DataColumn(data, homeViewModel = homeViewModel)
    }

    MyBackdrop(homeViewModel) { homeViewModel.changeBackdropState() }


    AlertComponent(
      visible = homeViewModel.alertState,
      onDismiss =  { homeViewModel.changeAlertState() },
      confirmButton = {
        TextButton(onClick = {
            viewModel.deleteDataById(homeViewModel.alertStateDeleteId)
            homeViewModel.changeAlertState()
          }
        ) {
        Text(text = stringResource(R.string.yes), textDecoration = TextDecoration.Underline)
      }},
      text = { Text(text = "${stringResource(R.string.are_you_sure_to_delete)}\n${homeViewModel.alertStateDeleteString}") }
    )

  }
}


@Composable
fun TodaySummary(
  viewModel: MainViewModel = hiltViewModel()
){
  val totalData by viewModel.getDataToday().collectAsState(initial = emptyList())
  val spendingTotal = Utils.totalDataString(totalData, "Spending")
  val incomeTotal = Utils.totalDataString(totalData, "Income")

  Card(
    modifier = cardModifier,
    backgroundColor = AlmostBlack
  ) {
    Column(modifier = Modifier.padding(8.dp)) {
      Text(text = stringResource(R.string.today))
      Spacer(modifier = Modifier.height(10.dp))
      Text(text = "${stringResource(R.string.spending)}: $spendingTotal")
      Text(text = "${stringResource(R.string.income)}: $incomeTotal")
    }
  }
}

@Composable
fun Summary(
  onClick: () -> Unit,
  viewModel: MainViewModel = hiltViewModel(),
  homeViewModel: HomeViewModel
) {
  val data by viewModel.allData.collectAsState(initial = emptyList())
  val balance = Utils.convertText("${Utils.totalBalance(data)}")
  homeViewModel.balanceValue = balance

  Card(
    modifier = cardModifier,
    backgroundColor = AlmostBlack
  ) {
    Column(modifier = Modifier.padding(8.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ){
        Text(
          text = stringResource(R.string.Balance),
          fontSize = MaterialTheme.typography.h5.fontSize
        )
        IconButton(onClick = {
          onClick.invoke()
          homeViewModel.backdropBalance()
        }) {
          Icon(painterResource(R.drawable.ic_edit), null)
        }
      }
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = balance,
        fontSize = MaterialTheme.typography.h5.fontSize
      )
      // button
      Row {
        OutlinedButton(
          onClick = {
            onClick.invoke()
            homeViewModel.backdropSpend()
          }
        ) {
          Text(text = stringResource(R.string.Spend))
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedButton(
          onClick = {
            onClick.invoke()
            homeViewModel.backdropIncome()
          }
        ) {
          Text(text = stringResource(R.string.Income))
        }
      }
    }
  }

}

@Composable
fun DataColumn(
  data: List<Data>,
  homeViewModel: HomeViewModel
){
  val ctx = LocalContext.current

  AnimatedVisibility(
    visible = !homeViewModel.backdropState,
    enter = fadeIn(),
    exit = fadeOut()
  ) {
    LazyColumn{
      items(data
        .filterNot { it.category == "balanceSpending" || it.category == "balanceIncome" }
        .filter { it.date == LocalDate.now().toString()  }
      ){

        val category = if(it.category == "Spending")
          ctx.getString(R.string.spending) else ctx.getString(R.string.income)
        val value = "$category: ${Utils.convertText(it.value.toString())}"
        Column(
          modifier = Modifier
            .padding(5.dp)
            .clickable {
              homeViewModel.changeAlertStateDeleteString(value)
              homeViewModel.changeAlertStateDeleteId(it.id)
              homeViewModel.changeAlertState()

            },
          verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
          Text(text = value)
          Divider()
        }
      }
    }
  }
}
