package com.nursyah.finance.presentation.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nursyah.finance.R

@Composable
fun HowToUseScreen(navHostController: NavHostController) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.how_to_use),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        Text(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            text = stringResource(R.string.how_to_use_content)
        )
    }
}