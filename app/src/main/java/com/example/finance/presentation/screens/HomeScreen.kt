package com.example.finance.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finance.R
import com.example.finance.core.Utils
import com.example.finance.db.model.Data
import com.example.finance.presentation.components.AlertComponent
import com.example.finance.presentation.components.BackdropContent
import com.example.finance.presentation.components.MainViewModel
import com.example.finance.presentation.theme.AlmostBlack
import java.text.SimpleDateFormat
import java.util.*


val homeViewModel: HomeViewModel = HomeViewModel()
@SuppressLint("SimpleDateFormat")
@Composable
fun HomeScreen(
  viewModel: MainViewModel = hiltViewModel(),
){
  val data by viewModel.allData.collectAsState(initial = emptyList())
  homeViewModel.changeBalance(data)

  Surface {
    Column(
      Modifier
        .padding(10.dp)
        .blur(100.dp)) {
      Summary(onClick = { homeViewModel.changeBackdropState() })
      Spacer(modifier = Modifier.height(15.dp))
      TodaySummary()
      Spacer(modifier = Modifier.height(15.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
      ) {
        val calendar = Calendar.getInstance()
        val date = SimpleDateFormat("dd-MMMM-yyyy").format(calendar.time)
        Text(
          text = date,
          modifier = Modifier.padding(horizontal = 5.dp))
      }
      DataColumn(data)
    }


    AlertComponent(
      visible = homeViewModel.alertState,
      onDismiss =  { homeViewModel.changeAlertState() },
      confirmButton = {
        TextButton(onClick = {
            viewModel.deleteAllData(homeViewModel.alertStateDeleteId)
            homeViewModel.changeAlertState()
          }
        ) {
        Text(text = "Yes", textDecoration = TextDecoration.Underline)
      }},
      text = { Text(text = "Are you sure to delete\n${homeViewModel.alertStateDeleteString}") }
    )

    BackdropContent(homeViewModel.backdropState) {
      SpendingIncomeBackdrop(
        onClose = { homeViewModel.changeBackdropState() }
      )
    }

  }
}


@Composable
fun TodaySummary(
  viewModel: MainViewModel = hiltViewModel()
){
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp)),
    backgroundColor = AlmostBlack
  ) {
    val totalData by viewModel.getDataToday().collectAsState(initial = emptyList())
    val spendingTotal = Utils.totalDataString(totalData, "Spending")
    val incomeTotal = Utils.totalDataString(totalData, "Income")

    Column(modifier = Modifier.padding(8.dp)) {
      Text(text = "Today")
      Spacer(modifier = Modifier.height(10.dp))
      Text(text = "Spending: $spendingTotal")
      Text(text = "Income: $incomeTotal")
    }
  }
}

@Composable
fun SpendingIncomeBackdrop(
  onClose: () -> Unit,
  viewModel: MainViewModel = hiltViewModel(),
){
  var value by remember { mutableStateOf("") }
  val state = if(homeViewModel.backdropStateValue == "Spend") "Spending" else "Income"
  val focusManager = LocalFocusManager.current
  val focusRequester = remember {FocusRequester()}

  //spending
  val stopSpending =
    state == "Spending" && (homeViewModel.balance - Utils.convertToLong(value)) <= 0


  Surface {
    Column {
      // header
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
      ) {
        TextButton(onClick = { }, enabled = false) {
          Text(text = state, color = Color.White)
        }

        IconButton(
          onClick = {
            focusManager.clearFocus()
            onClose.invoke()
          }
        ) {
          Icon(painter = painterResource(id = R.drawable.ic_close), contentDescription = null)
        }
      }
      Divider()
      Text(
        text = Utils.convertText(value),
        fontSize = MaterialTheme.typography.h5.fontSize,
        color = if(value == "" || stopSpending) Color.DarkGray else Color.White,
        modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
      )

      //show keyboard immediate
      LaunchedEffect(Unit){
        focusRequester.requestFocus()
      }

      OutlinedTextField(
        value = value,
        onValueChange = {if(it.length <= 13) value = it },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions(onDone = {
          focusManager.clearFocus()
          if(value != "" && !stopSpending){
            val category = if(homeViewModel.balanceSwitch) "balance$state" else state
            val data = Data(category = category, item = state, value = Utils.convertToLong(value))
            viewModel.addData(data)
            onClose.invoke()
          }
        }),
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .focusRequester(focusRequester)
      )
      Spacer(modifier = Modifier.height(10.dp))

      OutlinedButton(
        onClick = {
          if(value != "" && !stopSpending){
            val category = if(homeViewModel.balanceSwitch) "balance$state" else state
            val data = Data(category = category, item = state, value = Utils.convertToLong(value))
            viewModel.addData(data)
            onClose.invoke()
          }
        },
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(state)
      }
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { homeViewModel.changeBalanceSwitch() }
      ) {
        Switch(
          checked = homeViewModel.balanceSwitch,
          onCheckedChange = { homeViewModel.changeBalanceSwitch(it) })
        Text(text = "balance")
      }
      if(stopSpending)
        Text(
          text = "You can't spending if you didn't have enough balance",
          fontSize = MaterialTheme.typography.subtitle2.fontSize,
          color = Color.White.copy(alpha = .7f),
          textAlign = TextAlign.Center,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
        )
    }
  }
}

@Composable
fun Summary(
  onClick: () -> Unit,
  viewModel: MainViewModel = hiltViewModel(),
) {
  val data by viewModel.allData.collectAsState(initial = emptyList())
  val balance = Utils.convertText("${Utils.totalBalance(data)}")

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp)),
    backgroundColor = AlmostBlack
  ) {
    Column(modifier = Modifier.padding(8.dp)) {
      Text(
        text = "Balance",
        fontSize = MaterialTheme.typography.h5.fontSize
      )
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
          Text(text = "Spend")
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedButton(
          onClick = {
            onClick.invoke()
            homeViewModel.backdropIncome()
          }
        ) {
          Text(text = "Income")
        }
      }
    }
  }

}

@Composable
fun DataColumn(
  data: List<Data>,
){
  LazyColumn{
    items(data.filterNot { it.category == "balanceSpending" || it.category == "balanceIncome" }){
      val value = "${it.category}: ${Utils.convertText(it.value.toString())}"
      Column(
        modifier = Modifier
          .padding(5.dp)
          .clickable {
            homeViewModel.changeAlertStateDeleteString(value)
            homeViewModel.changeAlertStateDeleteId(it.id)
            homeViewModel.changeAlertState()
            println("test")
          },
        verticalArrangement = Arrangement.spacedBy(5.dp)
      ) {
        Text(text = value)
        Divider()
      }
    }
  }
}
