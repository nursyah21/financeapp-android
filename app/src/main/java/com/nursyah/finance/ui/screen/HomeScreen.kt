package com.nursyah.finance.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nursyah.finance.core.Utils
import com.nursyah.finance.core.Utils.Companion.enterAnimatedFade
import com.nursyah.finance.core.Utils.Companion.enterAnimatedSlideVertical
import com.nursyah.finance.core.Utils.Companion.exitAnimatedFade
import com.nursyah.finance.ui.components.BackdropContent
import com.nursyah.finance.ui.theme.AlmostBlack
import com.nursyah.finance.ui.theme.BlueGray
import com.nursyah.finance.ui.theme.paddingDimens
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(){

}

@Composable
fun HomeScreen(
  enabled: Boolean = false,
  padding: PaddingValues
){
  var spendingDialog by remember {mutableStateOf(false)}

  AnimatedVisibility(
    visible = enabled,
    modifier = Modifier.padding(padding),
    enter = enterAnimatedFade,
    exit = exitAnimatedFade
  ) {
    val scrollState = rememberScrollState()
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .scrollable(scrollState, Orientation.Vertical)
    ) {
      Summary(
        stateSpending = spendingDialog,
        onClickSpending = { spendingDialog = !spendingDialog }
      )
    }
  }
}

@Composable
fun Summary(
  onClickSpending: () -> Unit,
  stateSpending: Boolean
){
  var states by remember { mutableStateOf("") }

  Column(
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.padding(paddingDimens)
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      backgroundColor = AlmostBlack
    ) {
      Column(
        modifier = Modifier.padding(paddingDimens)
      ) {
        Text(
          text = "Balance",
          fontSize = MaterialTheme.typography.h5.fontSize
        )
        Spacer(modifier = Modifier.height(10.dp))
        val balanceValue = Utils.formatMoney("1000")
        Text(
          balanceValue,
          fontSize = MaterialTheme.typography.h6.fontSize
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row {
          //Spending button
          val scope = rememberCoroutineScope()
          OutlinedButton(
            onClick = {
              scope.launch {
                states = "Spending"
                onClickSpending.invoke()
              }
            } ,
            border = ButtonDefaults.outlinedBorder
          ) {
            Text(text = "Spend")
          }
          Spacer(modifier = Modifier.width(15.dp))
          //Income Button
          OutlinedButton(
            onClick = {
              scope.launch {
                states = "Income"
                onClickSpending.invoke()
              }
            },
            border = ButtonDefaults.outlinedBorder
          ) {
            Text(text = "Income")
          }
        }
      }
    }
  }

  AnimatedVisibility(visible = stateSpending,
    enter = enterAnimatedSlideVertical,
    exit = slideOutVertically(targetOffsetY = {it} /*if we separated this animated will show error*/
    )
  ) {
    SpendingDialog(onClickSpending, states)
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpendingDialog(
  onDismiss: () -> Unit,
  states: String = ""
){
  BackdropScaffold(
    appBar = { /**/ },
    backLayerContent = {  },
    frontLayerContent = {
      BackdropContent(
        onDismiss,
        onDismiss,
        states
      )
    },
    backLayerBackgroundColor = BlueGray,
    frontLayerBackgroundColor = AlmostBlack,
    gesturesEnabled = false
  )
}
