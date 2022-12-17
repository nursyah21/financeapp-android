package com.nursyah.finance.presentation.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.nursyah.finance.R


enum class ItemNav(
  val itemName: String,
  val itemIcon: Int
){
  HOME(itemName = "home", itemIcon = R.drawable.ic_dashboard),
  STATS(itemName = "stats", itemIcon = R.drawable.ic_stats),
  SETTINGS(itemName = "settings", itemIcon = R.drawable.ic_settings)
}

@Composable
fun Navbar(
  onClick: (String) -> Unit,
  value: String,
){
  BottomNavigation {
    ItemNav.values().forEach {
      BottomNavigationItem(
        selected = it.itemName == value,
        onClick = { onClick(it.itemName) },
        label = { Text(text = it.itemName)},
        icon = { Icon(painter = painterResource(id = it.itemIcon), contentDescription = null)}
      )
    }
  }
}