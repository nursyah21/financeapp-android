package com.example.finance.presentation.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun AlertComponent(
  visible: Boolean = false,
  onDismiss: () -> Unit,
  confirmButton: @Composable () -> Unit,
  title: String = "Confirm",
  text: @Composable () -> Unit
){
  if(visible)
    AlertDialog(
      onDismissRequest = onDismiss,
      title = {
        Text(text = title)
      },
      text = text,
      confirmButton = confirmButton
    )
}