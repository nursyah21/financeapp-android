package com.nursyah.finance.presentation.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nursyah.finance.R

@Composable
fun AlertComponent(
  visible: Boolean = false,
  onDismiss: () -> Unit,
  confirmButton: @Composable () -> Unit,
  title: String = stringResource(R.string.confirm),
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