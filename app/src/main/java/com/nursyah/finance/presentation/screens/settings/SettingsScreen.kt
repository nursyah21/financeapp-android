@file:OptIn(ExperimentalPermissionsApi::class)

package com.nursyah.finance.presentation.screens.settings

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.nursyah.finance.BuildConfig
import com.nursyah.finance.R
import com.nursyah.finance.presentation.components.MainViewModel
import com.nursyah.finance.core.Utils
import com.nursyah.finance.presentation.components.AlertComponent
import com.google.accompanist.permissions.*


@Composable
fun SettingsScreen(
  settingsViewModel: SettingsViewModel = hiltViewModel(),
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
        "version ${BuildConfig.VERSION_NAME}",
        fontSize = 14.sp,
      )
      Text(
        text = "OpenSource Libraries",
        textDecoration = TextDecoration.Underline,
        fontSize = 14.sp,
        color = Color.White.copy(alpha = .6f),
        modifier = Modifier.clickable { settingsViewModel.activityOpenSource() }
      )

    }
  }

}

@Composable
fun BackupRestoreData(
  settingsViewModel: SettingsViewModel = hiltViewModel(),
  mainViewModel: MainViewModel = hiltViewModel(),
) {
  val permission = rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent(),
    onResult = {
      if(it != null)
        settingsViewModel.restoreData(it)
    }
  )

  Column {
    Row {
      TextButton(onClick = {
        settingsViewModel.backupAlert()
      }) {
        Text(text = "Backup Data", textDecoration = TextDecoration.Underline)
      }
      Spacer(modifier = Modifier.width(8.dp))
      TextButton(onClick = {
        settingsViewModel.restoreAlert()
      }) {
        Text(text = "Restore Data", textDecoration = TextDecoration.Underline)
      }
    }
    TextButton(onClick = {
      settingsViewModel.deleteAlert()
    }) {
      Text(text = "Delete Data", textDecoration = TextDecoration.Underline)
    }
  }
  val context = LocalContext.current

  AlertSettings(
    confirmButton = {
      when(settingsViewModel.stateBackupRestore){
        "backup" -> {
          if(permission.status.isGranted) settingsViewModel.backupData()
          else {
            permission.launchPermissionRequest()
            if(!permission.status.shouldShowRationale)
              Utils.showToast(context, "Please enable storage permission")
          }
        }
        "restore" -> {
          if(permission.status.isGranted) launcher.launch("text/*")
          else {
            permission.launchPermissionRequest()
            if (!permission.status.isGranted)
              Utils.showToast(context, "Please enable storage permission")
          }
        }
        "delete" -> mainViewModel.deleteAllData()
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
      title = "Backup Data"
      text = "All data will be backup to your devices, are you sure"
    }
    "restore" -> {
      title = "Restore Data"
      text = "New data will be added, are you sure"
    }
    "delete" -> {
      title = "Delete Data"
      text = "All data will be erased, are you sure"
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
    arrayOf("Contribute in Github", context.getString(R.string.github_url)),
    arrayOf("Donate with ko-fi", context.getString(R.string.kofi_url)),
    arrayOf("Donate with trakteer", context.getString(R.string.trakteer_url)),
    arrayOf("Share with friend", context.getString(R.string.play_store)),
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
      Text(text = "want to support", textDecoration = TextDecoration.Underline)
    }

    Card(modifier = modifierSupportCard.clickable { supportCard = !supportCard }) {
      LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        items(list) {
          TextButton(onClick = {
            if(it[0] == "Share with friend")mainViewModel.shareText(it[1])
            else mainViewModel.openLink(it[1])
          }) {
            Text(text = it[0], textDecoration = TextDecoration.Underline)
          }
        }
      }
    }
  }

}
