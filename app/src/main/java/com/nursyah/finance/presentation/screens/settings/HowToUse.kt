package com.nursyah.finance.presentation.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nursyah.finance.R

@Composable
fun HowToUse(
  visible: Boolean,
  onDismiss: () -> Unit
) {

  AnimatedVisibility(
    visible = visible,
    enter = fadeIn(),
    exit = fadeOut()
  ) {
    Surface(
      color = Color.Black.copy(alpha = .4f)
    ) {
      Surface(
        Modifier
          .fillMaxSize()
          .pointerInput(Unit) {
            detectTapGestures(onTap = { onDismiss.invoke() })
          },
        color = Color.Black.copy(alpha = .4f)
      ) {}
      Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Content()
      }
    }

    /*AlertDialog(
      onDismissRequest = onDismiss,
      title = {*//* Text(ctx.getString(R.string.how_to_use))*//* },
      confirmButton = {
        *//*TextButton(onClick = onDismiss) { Text(ctx.getString(R.string.close)) }*//*
      },
      text = { Content() }
    )*/
  }
}

@Composable
private fun Content(){
  Card(Modifier.size(300.dp)) {
    Column(Modifier.padding(15.dp)) {
      //make text header in center
      Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          stringResource(R.string.how_to_use),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold
        )
      }
      Spacer(Modifier.padding(vertical = 8.dp))
      Text(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        text = stringResource(R.string.how_to_use_content)
      )
    }
  }
}