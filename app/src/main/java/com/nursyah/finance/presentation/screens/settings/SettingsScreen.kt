package com.nursyah.finance.presentation.screens.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nursyah.finance.R
import com.nursyah.finance.core.Constants
import com.nursyah.finance.navigation.MainDestination
import com.nursyah.finance.presentation.components.AlertComponent
import com.nursyah.finance.presentation.components.MainViewModel
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    text = stringResource(id = R.string.about),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        item {
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.backup_data)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .clickable {
                        scope.launch {
                            settingsViewModel.backupAlert()
                        }
                    },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    headlineColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        }

        item {
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.restore_data)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .clickable {
                        scope.launch {
                            settingsViewModel.restoreAlert()
                        }
                    },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    headlineColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        }

        item {
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.delete_data)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .clickable {
                        scope.launch {
                            settingsViewModel.deleteAlert()
                        }
                    },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    headlineColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        }

        item {
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.how_to_use)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .clickable {
                        scope.launch {
                            navHostController.navigate(MainDestination.SettingsRoute.HowToUse.route)
                        }
                    },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    headlineColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        }

        item {
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.want_to_support)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .clickable {
                        scope.launch {
                            settingsViewModel.support()
                        }
                    },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    headlineColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        }
    }

    if (settingsViewModel.support) {
        SupportAlert {
            scope.launch {
                settingsViewModel.support()
            }
        }
    }
    BackupRestoreData()
}

@Composable
fun BackupRestoreData(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) settingsViewModel.restoreData(it)
        }
    )


    Column {
        val scrollState = rememberScrollState()
        val ctx = LocalContext.current
        val modifierClickable =
            if (settingsViewModel.stateBackupRestore == Constants.SETTINGS_STATE_BACKUP) {
                Modifier.clickable {
                    openFolderCsv(settingsViewModel, ctx)
                }
            } else Modifier
        //status delete,backup, and restore
        Row(
            Modifier
                .padding(5.dp)
                .horizontalScroll(scrollState)
        ) {

            Text(
                modifier = modifierClickable,
                text = settingsViewModel.statusBackupRestore,
                color = Color.White.copy(alpha = .7f)
            )
        }
    }

    AlertSettings(
        confirmButton = {
            when (settingsViewModel.stateBackupRestore) {
                "backup" -> {
                    settingsViewModel.backupData()
                }

                "restore" -> {
                    launcher.launch("text/*")
                }

                "delete" -> {
                    settingsViewModel.deleteData()
                }
            }
        }
    )
}


private fun openFolderCsv(settingsViewModel: SettingsViewModel, ctx: Context) {
    val file = settingsViewModel.filePath

    //open new file
    val newIntent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(
            Uri.parse(file?.path),
            "*/*"
        )
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    ctx.startActivity(newIntent)
}

@Composable
fun AlertSettings(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    confirmButton: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }


    when (settingsViewModel.stateBackupRestore) {
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
                confirmButton()
                settingsViewModel.changeAlertDialog()
            }) {
                Text(text = "Yes", textDecoration = TextDecoration.Underline)
            }
        },
        title = title
    ) {
        Text(text = text)
    }
}

data class SupportList(val name: String, val url: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportAlert(
    onDismiss: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val appUrl = stringResource(id = R.string.play_store)
    val supportList = listOf(
        SupportList(
            name = stringResource(id = R.string.contribute_in_github),
            url = stringResource(id = R.string.github_url)
        ),
        SupportList(
            name = stringResource(id = R.string.donate_with_kofi),
            url = stringResource(id = R.string.kofi_url)
        ),
        SupportList(
            name = stringResource(id = R.string.donate_with_trakteer),
            url = stringResource(id = R.string.trakteer_url)
        )
    )
    AlertDialog(onDismissRequest = { onDismiss() }) {
        LazyColumn(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.medium
                )
                .fillMaxWidth()
        ) {
            items(supportList) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = it.name
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clip(shape = MaterialTheme.shapes.medium)
                        .clickable {
                            scope.launch {
                                uriHandler.openUri(it.url)
                            }
                        },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        headlineColor = MaterialTheme.colorScheme.onSecondary
                    )
                )
            }

            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.share_with_friend)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clip(shape = MaterialTheme.shapes.medium)
                        .clickable {
                            scope.launch {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        appUrl
                                    )
                                    type = "text/plain"
                                }

                                val shareIntent = Intent.createChooser(sendIntent, null)
                                context.startActivity(shareIntent)
                            }
                        },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        headlineColor = MaterialTheme.colorScheme.onSecondary
                    )
                )
            }
        }
    }
}