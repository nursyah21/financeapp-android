package com.nursyah.finance.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nursyah.finance.core.Utils


@Composable
fun StatsScreen(
  enabled: Boolean = false,
  padding: PaddingValues
){
  AnimatedVisibility(
    visible = enabled,
    modifier = Modifier.padding(padding),
    enter = Utils.enterAnimatedFade,
    exit = Utils.exitAnimatedFade
  ) {
    Surface(
      modifier = Modifier.fillMaxSize()
    ) {
      Text(text = "stats screen")
    }
  }
}