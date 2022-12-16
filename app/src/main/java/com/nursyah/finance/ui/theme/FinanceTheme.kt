package com.nursyah.finance.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

val paddingDimens = PaddingValues(horizontal = 10.dp, vertical = 15.dp)

val BlueGray = Color(0xff000a12)
val AlmostBlack = Color(0xff1F272D)

val ColorPalette = darkColors(
  primary = Color.White,
  surface = BlueGray,
  onSurface = Color.White,
  background = BlueGray,
  onBackground = Color.White
)

@Composable
fun FinanceTheme(content: @Composable () -> Unit){
  val view = LocalView.current
  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
    (view.context as Activity).window.statusBarColor = BlueGray.toArgb()
    (view.context as Activity).window.navigationBarColor = BlueGray.toArgb()
  }

  MaterialTheme(colors = ColorPalette, content = content, typography = Typography)
}