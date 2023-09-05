package com.nursyah.finance.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.nursyah.finance.R

enum class ItemNav(
  val itemName: Int,
  val itemIcon: Int,
){
  HOME(itemName = R.string.home, itemIcon = R.drawable.ic_dashboard),
  STATS(itemName = R.string.stats, itemIcon = R.drawable.ic_stats),
  SETTINGS(itemName = R.string.settings, itemIcon = R.drawable.ic_settings)
}


@Composable
fun Navbar(
  onClick: (String) -> Unit,
  value: String,
  navController: NavHostController,
){
  val ctx = LocalContext.current
  NavigationBar {
    ItemNav.values().forEach {
      val itemName = ctx.getString(it.itemName)
      NavigationBarItem(
        selected = itemName == value,
        onClick = {
          onClick(itemName)
          if(navController.currentDestination?.route != itemName) {
            navController.popBackStack()
            navController.navigate(itemName)

          }
        },
        label = { Text(text = itemName)},
        icon = { Icon(painter = painterResource(id = it.itemIcon), contentDescription = null)}
      )
    }
  }
}