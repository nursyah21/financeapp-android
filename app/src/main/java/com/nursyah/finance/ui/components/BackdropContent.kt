package com.nursyah.finance.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nursyah.finance.R
import com.nursyah.finance.core.Utils
import com.nursyah.finance.ui.screen.HomeScreenViewModel
import com.nursyah.finance.ui.theme.AlmostBlack
import kotlinx.coroutines.launch

@Composable
fun BackdropContent(
  onClose: () -> Unit,
  updateOnClick: () -> Unit,
  states: String = ""
){
  Surface(modifier = Modifier.fillMaxSize(), color = AlmostBlack) {
    Column(
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.End,
      modifier = Modifier.padding(5.dp)
    ) {
     Content(onClose, updateOnClick, states)
    }
  }
}

@Composable
fun Content(
  onClose: () -> Unit,
  updateOnClick: () -> Unit,
  states: String = ""
){
  //header spending
  Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier.fillMaxWidth()
  ){
    TextButton(onClick = {  }, enabled = false) {
      Text(text = states, color = Color.White)
    }
    IconButton(onClick = onClose) {
      Icon(painter = painterResource(id = R.drawable.close), contentDescription = null)
    }
  }
  Divider()
  //summary
  var newValue by remember { mutableStateOf("") }
  var newItem by remember { mutableStateOf("") }
  val value = Utils.formatMoney(newValue)

  Card(modifier = Modifier
    .fillMaxWidth()
    .padding(vertical = 10.dp, horizontal = 5.dp)
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Text(
        text = newItem.ifEmpty { "Description" },
        fontSize = MaterialTheme.typography.h6.fontSize,
        maxLines = 1,
        color = if(newItem.isEmpty()) Color(0x44ffffff) else Color.White
      )
      Spacer(modifier = Modifier. height(5.dp))
      Text(
        text = value,
        maxLines = 1,
        fontSize = MaterialTheme.typography.h6.fontSize,
        color = if(newValue.isEmpty()) Color(0x44ffffff) else Color.White
      )
    }
  }

  Field(
    value = newValue,
    onValueChange = {if(it.length <= 10) newValue = it},
    item = newItem,
    onItemChange = {if(it.length <= 16) newItem = it},
    updateOnClick = updateOnClick
  )


}

@Composable
fun Field(
  value: String,
  onValueChange: (String) -> Unit,
  item: String,
  onItemChange: (String) -> Unit,
  updateOnClick: () -> Unit
){
  val scope = rememberCoroutineScope()
  val focusManager = LocalFocusManager.current

  TextField(
    value = item,
    label = { Text("description") },
    onValueChange = {
      scope.launch {
        onItemChange(it)
      }
    },
    singleLine = true,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Text
    ),
    keyboardActions = KeyboardActions(
      onDone = { focusManager.moveFocus(FocusDirection.Down) }
    ),
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 10.dp, horizontal = 5.dp)
  )

  //value
  TextField(
    value = value,
    label = { Text("value") },
    onValueChange = {
      scope.launch {
        onValueChange(it)
      }
    },
    singleLine = true,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Number
    ),
    keyboardActions = KeyboardActions(
      onDone = {
        focusManager.clearFocus()
      }
    ),
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 10.dp, horizontal = 5.dp)
  )

  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    SendToFireBase(updateOnClick, hashMapOf("item" to item, "value" to value))
  }
}

@Composable
fun SendToFireBase(
  onClick: () -> Unit,
  _data: HashMap<String, String> = hashMapOf("item" to "","value" to "")
){

  val context = LocalContext.current
  val homeScreenViewModel = HomeScreenViewModel(context)
  Column(
    modifier = Modifier.fillMaxWidth()
  ) {
    val sendData = !(_data["item"] == "" && _data["value"] == "")
    TextButton(
      onClick = {
        {}.let{ if(sendData) onClick.invoke()
          .also { homeScreenViewModel.sendSpending(_data) } else showToast(context)
        }

      },
      border = ButtonDefaults.outlinedBorder,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(text = "Enter")
    }
  }
}

fun showToast(context: Context){
  Toast.makeText(context, "description and value can't be empty", Toast.LENGTH_SHORT).show()
}