package com.nursyah.finance.presentation.components

import android.content.res.Resources
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nursyah.finance.presentation.theme.Dark

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BackdropContent(
  visible: Boolean,
  content: @Composable () -> Unit,
){
  val height = if(Resources.getSystem().displayMetrics.heightPixels > 1280) 160.dp else 95.dp

  AnimatedVisibility(
    visible = visible,
    enter = slideInVertically(initialOffsetY = {it}),
    exit = slideOutVertically(targetOffsetY = {it})
  ) {
    BackdropScaffold(
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
      gesturesEnabled = false
    )
  }
}