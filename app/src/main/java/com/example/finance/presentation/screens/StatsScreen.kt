package com.example.finance.presentation.screens

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp


@Composable
fun StatsScreen() {
  ChartData()
}


@Composable
fun ChartData(){
  //set text
  val density = LocalDensity.current
  val paint = Paint().apply {
    color = Color.White.toArgb()
    textSize = density.run { 14.sp.toPx() }
  }

 Canvas(modifier = Modifier.fillMaxSize()){
   val width = size.width
   val height = size.height

   drawContext.canvas.nativeCanvas.drawText(
     "test",width / 2, height / 2,paint
   )

   drawLine(
     start = Offset(0f, 0f),
     end = Offset(width/2, height/2),
     color = Color.Red,
     strokeWidth = 2f
   )

   drawLine(
     start = Offset(width/2, height/2),
     end = Offset(width, height),
     color = Color.Red,
     strokeWidth = 3f
   )

   drawLine(
     start = Offset(0f, 0f),
     end = Offset(width/2, height/2),
     color = Color.White
   )

   drawLine(
     start = Offset(width/2, height/2),
     end = Offset(width, height/2),
     color = Color.White
   )
 }
}