package com.nursyah.finance.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nursyah.finance.R
import com.nursyah.finance.core.Utils
import com.nursyah.finance.db.model.Data
import com.nursyah.finance.presentation.components.MainViewModel

@Composable
fun MyBackdrop(
  homeViewModel: HomeViewModel,
  onClose: () -> Unit,
) {
  //background
  AnimatedVisibility(
    visible = homeViewModel.backdropState,
    enter = slideInVertically(initialOffsetY = {it}),
    exit = slideOutVertically(targetOffsetY = {it})
  ) {
    Surface(
      Modifier
        .fillMaxSize()
        .blur(30.dp),
      color = Color.Black.copy(alpha = .6f)
    ) {
      Content(homeViewModel, onClose)
    }
  }
}

@Composable
private fun Content(
  homeViewModel: HomeViewModel,
  onClose: () -> Unit
) {
  var value by remember { mutableStateOf("") }
  val state = when(homeViewModel.backdropStateValue){
    "Spend" -> "Spending"
    "Income" -> "Income"
    "Balance" -> "Balance"
    else -> ""
  }

  val ctx = LocalContext.current
  val textState =  when(state){
    "Spending" -> ctx.getString(R.string.spending)
    "Income" -> ctx.getString(R.string.income)
    "Balance" -> ctx.getString(R.string.Balance)
    else -> ""
  }

  //if(state == "Spending") ctx.getString(R.string.spending) else ctx.getString(R.string.income)

  /*val swipeState = rememberSwipeableState(0)
  val sizePx = with(LocalDensity.current){48.dp.toPx()}
  val anchors = mapOf(0f to 0, sizePx to 1)*/

  /*TODO in new update create swipe function to close backdrop)*/
  Column(
    modifier= Modifier
      .fillMaxSize()
      .padding(8.dp)
      /*.swipeable(
        state = swipeState,
        anchors = anchors,
        thresholds = { _, _ -> FractionalThreshold(0f) },
        orientation = Orientation.Vertical
      )*/,
    verticalArrangement = Arrangement.Bottom
  ) {
    Box {
      KeyboardNumber(
        value,
        textState,
        homeViewModel,
        onClose = onClose,
      ) { value = it }
    }
  }
}


@Composable
private fun KeyboardNumber(
  value: String,
  textState: String,
  homeViewModel: HomeViewModel,
  viewModel: MainViewModel = hiltViewModel(),
  onClose: () -> Unit,
  onChange: (String) -> Unit,
){

  Column(
    modifier= Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Bottom
  ) {
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
      color = if (Utils.convertToLong(value) == 0L) Color.DarkGray else Color.White,
      modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
    )

    //get always positive value for prevValue
    val ctx = LocalContext.current
    val prevValue =
      Utils.convertToLong(homeViewModel.balanceValue.replace("-","").replace(",",""))
    val nowValue = Utils.convertToLong(value)

    OutlinedButton(
      onClick = {
        if(Utils.convertToLong(value) != 0L){
          var itemValue =  Utils.convertToLong(value)
          //make data store in balance instead to spend or income
          var category = textState
          if(textState == ctx.getString(R.string.Balance)) {
            val state = if(nowValue > prevValue) "Spending" else "Income"

            itemValue = if (nowValue > prevValue) prevValue - nowValue else prevValue + nowValue
            category = "balance$state"
          }

          val data = Data(category = category, item = textState, value = itemValue)
          viewModel.addData(data)
          onClose.invoke()
        }
      },
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(textState)
    }
    Spacer(modifier = Modifier.padding(vertical = 8.dp))

    //row1
    RowKeyboard(listOf("1", "2", "3"), onChange, value)
    //row2
    RowKeyboard(listOf("4", "5", "6"), onChange, value)
    //row3
    RowKeyboard(listOf("7", "8", "9"), onChange, value)
    //row4
    RowKeyboard(listOf("000","0","del"), onChange, value)
  }
}

@Composable
private fun RowKeyboard(row: List<String>, onChange: (String) -> Unit, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    row.forEach {
      OutlinedButton(
        modifier = Modifier.weight(1f),
        onClick = {
          if (it == "del") onChange(value.dropLast(1)) else
            if (value.length < 12) onChange(value + it)
        }) {
        if (it == "del")
          Icon(painter = painterResource(R.drawable.ic_backspace), contentDescription = null)
        else Text(it)
      }
    }
  }
}