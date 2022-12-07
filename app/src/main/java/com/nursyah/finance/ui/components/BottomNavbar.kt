package com.nursyah.finance.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.nursyah.finance.ui.ItemNav

@Composable
fun BottomNavbar(onClick: (String) -> Unit, value: String){
  BottomNavigation {
    ItemNav.values().forEach {
      BottomNavigationItem(
        selected = it.itemName == value,
        onClick = { onClick(it.itemName) },
        label = { Text(text = it.itemName) },
        icon = { Icon(painter = painterResource(id = it.itemIcon), contentDescription = null) }
      )
    }
  }
}
