package com.nursyah.finance.presentation.screens.license

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.nursyah.finance.R

@Composable
fun LicenseScreen(
  navHostController: NavHostController
){
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      val ctx = LocalContext.current
      Text(
        text = stringResource(R.string.open_source_license),
        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
        modifier = Modifier.padding(5.dp)
      )
      IconButton(onClick = {
        navHostController.navigateUp()
      }) {
        Icon(painter = painterResource(R.drawable.ic_close), contentDescription = null)
      }
    }
    Divider()
    LibrariesContainer(Modifier.fillMaxSize())
  }
}