package com.nursyah.finance.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nursyah.finance.R
import com.nursyah.finance.core.Utils
import com.nursyah.finance.db.model.Data
import com.nursyah.finance.presentation.components.KeyboardNumber
import com.nursyah.finance.presentation.components.MainViewModel

@Composable
fun SpendingIncomeBackdrop(
  onClose: () -> Unit,
  viewModel: MainViewModel = hiltViewModel(),
  homeViewModel: HomeViewModel
){
  var value by remember { mutableStateOf("") }
  val state = if(homeViewModel.backdropStateValue == "Spend") "Spending" else "Income"

  //spending
  val stopSpending = state == "Spending" && (homeViewModel.balance - Utils.convertToLong(value)) < 0

  val ctx = LocalContext.current
  val textState = if(state == "Spending") ctx.getString(R.string.spending) else ctx.getString(R.string.income)

  Surface {
    Column {
      // header
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
      ) {
        //for label so we don't need clickable
        TextButton(onClick = { }, enabled = false) {
          Text(text = textState, color = Color.White)
        }

        IconButton(onClick = { onClose.invoke() }) {
          Icon(painter = painterResource(id = R.drawable.ic_close), contentDescription = null)
        }
      }
      Divider()
      Text(
        text = Utils.convertText(value),
        fontSize = MaterialTheme.typography.h5.fontSize,
        color = if(stopSpending || Utils.convertToLong(value) == 0L) Color.DarkGray else Color.White,
        modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
      )

      OutlinedButton(
        onClick = {
          if(Utils.convertToLong(value) != 0L && !stopSpending){
            //make data store in balance instead to spend or income
            val category = if(homeViewModel.balanceSwitch) "balance$state" else state
            val data = Data(category = category, item = state, value = Utils.convertToLong(value))
            viewModel.addData(data)
            onClose.invoke()
          }
        },
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(textState)
      }
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { homeViewModel.changeBalanceSwitch() }
      ) {
        Switch(
          checked = homeViewModel.balanceSwitch,
          onCheckedChange = { homeViewModel.changeBalanceSwitch(!it) })
        Text(text = stringResource(R.string.balance))
      }
      if(stopSpending) {
        Text(
          text = stringResource(R.string.not_enough_money),
          fontSize = MaterialTheme.typography.subtitle2.fontSize,
          color = Color.White.copy(alpha = .7f),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
        )
      }

      KeyboardNumber(value) { value = it }
    }
  }
}