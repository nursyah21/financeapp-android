package com.nursyah.finance.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nursyah.finance.R
import com.nursyah.finance.core.Utils.Companion.enterAnimated
import com.nursyah.finance.core.Utils.Companion.exitAnimated
import com.nursyah.finance.ui.theme.AlmostBlack
import com.nursyah.finance.ui.theme.BlueGray


@ExperimentalMaterialApi
@Composable
fun HomeScreen(
  enabled: Boolean = false,
  padding: PaddingValues
){
  var spendingDialog by remember { mutableStateOf(false) }

  AnimatedVisibility(
    visible = enabled,
    modifier = Modifier.padding(padding),
    enter = enterAnimated,
    exit = exitAnimated
  ) {
    Surface(
      modifier = Modifier.fillMaxSize()
    ) {
      Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        TextButton(
          onClick = { spendingDialog = !spendingDialog },
          border = ButtonDefaults.outlinedBorder
        ) {
          Text(text = "spend Button")
        }
      }

      AnimatedVisibility(
        visible = spendingDialog,
        enter = enterAnimated,
        exit = exitAnimated
      ) {
        SpendingDialog(
          onClose = { spendingDialog = !spendingDialog }
        )
      }
    }
  }
}

@ExperimentalMaterialApi
@Composable
fun SpendingDialog(
  onClose: () -> Unit
){
  BackdropScaffold(
    appBar = { /**/ },
    backLayerContent = {  },
    frontLayerContent = { BackdropContent(onClose) },
    backLayerBackgroundColor = BlueGray,
    frontLayerBackgroundColor = AlmostBlack,
    gesturesEnabled = false
  )
}

@Composable
fun BackdropContent(onClose: () -> Unit){
  Surface(modifier = Modifier.fillMaxSize(), color = AlmostBlack) {
    Column(
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.End,
      modifier = Modifier.padding(5.dp)
    ) {
      IconButton(onClick = onClose) {
        Icon(painter = painterResource(id = R.drawable.close), contentDescription = null)
      }
    }
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = "spending")
      SendToFireBase()
    }
  }
}

val homeScreenViewModel = HomeScreenViewModel()
@Composable
fun SendToFireBase(){
  TextButton(
    onClick = { homeScreenViewModel.sendSpending() },
    border = ButtonDefaults.outlinedBorder
  ) {
    Text(text = "send to firebase")
  }
  TextButton(
    onClick = { homeScreenViewModel.readSpending() },
    border = ButtonDefaults.outlinedBorder
  ) {
    Text(text = "read data firebase")
  }
}