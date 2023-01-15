package com.nursyah.finance.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nursyah.finance.R

@Composable
fun KeyboardNumber(
  value: String,
  onChange: (String) -> Unit,
){
  Column(
    modifier= Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Bottom
  ) {

    //row1
    RowKeyboard(listOf("1", "2", "3"), onChange, value)
    //row2
    RowKeyboard(listOf("4", "5", "6"), onChange, value)
    //row3
    RowKeyboard(listOf("7", "8", "9"), onChange, value)
    //row4
    RowKeyboard(listOf("000","0","del"), onChange, value)
  }
}

@Composable
private fun RowKeyboard(row: List<String>, onChange: (String) -> Unit, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    row.forEach {
      OutlinedButton(
        modifier = Modifier.weight(1f),
        onClick = {
          if (it == "del") onChange(value.dropLast(1)) else
            if (value.length < 12) onChange(value + it)
        }) {
        if (it == "del")
          Icon(painter = painterResource(R.drawable.ic_backspace), contentDescription = null)
        else Text(it)
      }
    }
  }
}