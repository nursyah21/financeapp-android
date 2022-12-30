package com.nursyah.finance.presentation.components

import android.content.res.Resources
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nursyah.finance.presentation.theme.Dark

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BackdropContent(
  visible: Boolean,
  onClose: () -> Unit,
  content: @Composable () -> Unit,
){
  //height to peek
  val height =  if(Resources.getSystem().displayMetrics.heightPixels > 1280) 260.dp else 100.dp
  val backdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)

  LaunchedEffect(backdropScaffoldState.isRevealed){
    if(backdropScaffoldState.isRevealed) {
      onClose.invoke()
    }
  }

  AnimatedVisibility(
    visible = visible,
    enter = slideInVertically(initialOffsetY = {it}),
    exit = slideOutVertically(targetOffsetY = {it})
  ) {
    BackdropScaffold(
      scaffoldState = backdropScaffoldState,
      appBar = {  },
      backLayerContent = { },
      frontLayerContent = {
        Surface(modifier = Modifier.fillMaxSize(), color = Dark){
          Column(modifier = Modifier.padding(8.dp)){
            content.invoke()
          }
        }
      },
      peekHeight = height,
      backLayerBackgroundColor = Color.Transparent,
      stickyFrontLayer = false,
      //gesturesEnabled = false
    )
  }
}