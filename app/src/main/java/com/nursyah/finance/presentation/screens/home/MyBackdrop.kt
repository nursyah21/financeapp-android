package com.nursyah.finance.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nursyah.finance.R
import com.nursyah.finance.core.Utils
import com.nursyah.finance.db.model.Data
import com.nursyah.finance.presentation.components.MainViewModel
import kotlin.math.roundToInt

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

@OptIn(ExperimentalMaterialApi::class)
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

  val swipeState = rememberSwipeableState(0)
  val sizePx = with(LocalDensity.current){200.dp.toPx()}
  val anchors = mapOf(0f to 0, sizePx to 10)

  /*TODO in new update create swipe function to close backdrop)*/
  Column(
    modifier= Modifier
      .fillMaxSize()
      .padding(8.dp)
      .swipeable(
        state = swipeState,
        anchors = anchors,
        thresholds = { _, _ -> FractionalThreshold(0f) },
        orientation = Orientation.Vertical
      ),
    verticalArrangement = Arrangement.Bottom
  ) {
    Box(
      Modifier
        .offset { IntOffset(0, swipeState.offset.value.roundToInt()) }
    ) {
      KeyboardNumber(
        value,
        textState,
        homeViewModel,
        onClose = onClose,
      ) { value = it }
    }
  }

  LaunchedEffect(swipeState.offset.value.roundToInt()){
    try{
      if(swipeState.offset.value.roundToInt() >= 250) {
        onClose.invoke()
        swipeState.animateTo(0)
      }else swipeState.animateTo(0)
    }catch (e:Exception){
      println(e)
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
  val ctx = LocalContext.current
  
  Column(
    modifier= Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Bottom
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
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
      color = if (Utils.convertToLong(value) == 0L && textState != ctx.getString(R.string.Balance)) Color.DarkGray else Color.White,
      modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
    )

    //get always positive value for prevValue
    val prevValueMinus = homeViewModel.balanceValue.startsWith("-")
    val prevValue =
      Utils.convertToLong(homeViewModel.balanceValue.replace("-","").replace(",",""))

    val nowValue = Utils.convertToLong(value)

    OutlinedButton(
      onClick = {
        if(Utils.convertToLong(value) != 0L || textState == ctx.getString(R.string.Balance)){
          var itemValue =  Utils.convertToLong(value)
          //make data store in balance instead to spend or income
          //there's two scenario first when previous value negative , two previous value positive
          var category = textState
          if(textState == ctx.getString(R.string.Balance)) {
            //previous positive
            var state = if(nowValue > prevValue) "Income" else "Spending"
            itemValue = if (nowValue > prevValue) nowValue - prevValue else prevValue - nowValue
            //previous negative
            if(prevValueMinus){
              state = "Income"
              itemValue = prevValue + nowValue
            }
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
          when(it){
            "del" -> onChange(value.dropLast(1))
            "000" -> if(value.length < 12 && value.isNotEmpty()) onChange(value + it)
            "0" -> if(value.isEmpty()) onChange(value + it)
            else -> if(value.length < 12) onChange(value + it)
          }
        }) {
        if (it == "del")
          Icon(painter = painterResource(R.drawable.ic_backspace), contentDescription = null)
        else Text(it)
      }
    }
  }
}