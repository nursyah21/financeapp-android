package com.nursyah.finance.presentation.screens.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nursyah.finance.BuildConfig
import com.nursyah.finance.R
import com.nursyah.finance.core.Constants
import com.nursyah.finance.presentation.components.AlertComponent
import com.nursyah.finance.presentation.components.MainViewModel


@Composable
fun SettingsScreen(
  navHostController: NavHostController,
  mainViewModel: MainViewModel = hiltViewModel(),
){

  Surface(
    modifier = Modifier.fillMaxSize()
  ) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)){
      Text("Finance", fontSize = MaterialTheme.typography.h6.fontSize)
      Divider()
      Text(text = stringResource(R.string.about), modifier = Modifier.padding(vertical = 8.dp))
      Spacer(modifier = Modifier.height(10.dp))
      BackupRestoreData()
      Divider()
      Content()
    }
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Bottom,
    ) {
      Text(
        "${stringResource(R.string.version)} ${BuildConfig.VERSION_NAME}",
        fontSize = 14.sp,
      )
      val ctx = LocalContext.current
      Text(
        text = "OpenSource Libraries",
        textDecoration = TextDecoration.Underline,
        fontSize = 14.sp,
        color = Color.White.copy(alpha = .6f),
        modifier = Modifier.clickable {
          navHostController.popBackStack()
          navHostController.navigate(ctx.getString(Constants.SCREEN_LICENSE))
        }
      )
      Text(
        text = "Privacy and Policy",
        textDecoration = TextDecoration.Underline,
        fontSize = 14.sp,
        color = Color.White.copy(alpha = .6f),
        modifier = Modifier.clickable {
          mainViewModel.openLink(ctx.getString(R.string.privacy_policy))
        }
      )
    }
  }

}

@Composable
fun BackupRestoreData(
  settingsViewModel: SettingsViewModel = hiltViewModel(),
) {

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent(),
    onResult = {
      if(it != null) settingsViewModel.restoreData(it)
    }
  )


  Column {
    Row {
      TextButton(onClick = {
        settingsViewModel.backupAlert()
      }) {
        Text(text = stringResource(R.string.backup_data),
          textDecoration = TextDecoration.Underline,
          color = Color.White
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      TextButton(onClick = {
        settingsViewModel.restoreAlert()
      }) {
        Text(text = stringResource(R.string.restore_data),
          textDecoration = TextDecoration.Underline,
          color = Color.White
        )
      }
    }
    TextButton(onClick = {
      settingsViewModel.deleteAlert()
    }) {
      Text(text = stringResource(R.string.delete_data), textDecoration = TextDecoration.Underline)
    }
    val scrollState = rememberScrollState()
    //status delete,backup, and restore
    Row(
      Modifier
        .padding(5.dp)
        .horizontalScroll(scrollState)){
      Text(
        text = settingsViewModel.statusBackupRestore,
        color = Color.White.copy(alpha = .7f)
      )
    }
  }


  AlertSettings(
    confirmButton = {
      when(settingsViewModel.stateBackupRestore){
        "backup" -> { settingsViewModel.backupData() }
        "restore" -> { launcher.launch("text/*") }
        "delete" -> { settingsViewModel.deleteData() }
      }
    }
  )
}

@Composable
fun AlertSettings(
  settingsViewModel: SettingsViewModel = hiltViewModel(),
  confirmButton:  () -> Unit,
){
  var title by remember { mutableStateOf("") }
  var text by remember { mutableStateOf("") }


  when(settingsViewModel.stateBackupRestore){
    "backup" -> {
      title = stringResource(R.string.backup_data)
      text = stringResource(R.string.confirm_backup_data)
    }
    "restore" -> {
      title = stringResource(R.string.restore_data)
      text = stringResource(R.string.confirm_restore_data)
    }
    "delete" -> {
      title = stringResource(R.string.delete_data)
      text = stringResource(R.string.confirm_delete_data)
    }
  }

  AlertComponent(
    visible = settingsViewModel.alertDialog,
    onDismiss = { settingsViewModel.changeAlertDialog() },
    confirmButton = {
      TextButton(onClick = {
        confirmButton.invoke()
        settingsViewModel.changeAlertDialog()
      }) {
        Text(text = "Yes", textDecoration = TextDecoration.Underline)
      }
    },
    title = title
  ){
    Text(text = text)
  }
}

@Composable
private fun Content(
  mainViewModel: MainViewModel = hiltViewModel(),
) {
  val context = LocalContext.current
  val list = listOf(
    arrayOf(stringResource(R.string.contribute_in_github), context.getString(R.string.github_url)),
    arrayOf(stringResource(R.string.donate_with_kofi), context.getString(R.string.kofi_url)),
    arrayOf(stringResource(R.string.donate_with_trakteer), context.getString(R.string.trakteer_url)),
    arrayOf(stringResource(R.string.share_with_friend), context.getString(R.string.play_store)),
  )
  var supportCard by remember { mutableStateOf(false) }
  var modifierSupportCard = Modifier
    .padding(horizontal = 15.dp)
    .fillMaxWidth()
    .animateContentSize()
  modifierSupportCard = if (supportCard) modifierSupportCard else modifierSupportCard.height(0.dp)

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    TextButton(onClick = { supportCard = !supportCard },
    ) {
      Text(text = stringResource(R.string.want_to_support), textDecoration = TextDecoration.Underline)
    }
    val ctx = LocalContext.current
    Card(modifier = modifierSupportCard.clickable { supportCard = !supportCard }) {
      LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        items(list) {
          TextButton(onClick = {
            if(it[0] == ctx.getString(R.string.share_with_friend))mainViewModel.shareText(it[1])
            else mainViewModel.openLink(it[1])
          }) {
            Text(text = it[0], textDecoration = TextDecoration.Underline)
          }
        }
      }
    }
  }

}
