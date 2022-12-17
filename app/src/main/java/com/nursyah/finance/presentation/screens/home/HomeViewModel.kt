package com.nursyah.finance.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nursyah.finance.core.Utils
import com.nursyah.finance.db.model.Data


class HomeViewModel: ViewModel(){
  var backdropState by mutableStateOf(false)
    private set

  fun changeBackdropState(){
    backdropState = !backdropState
  }

  var backdropStateValue by mutableStateOf("")
    private set
  fun backdropIncome()  { backdropStateValue = "Income" }
  fun backdropSpend()  { backdropStateValue = "Spend" }

  var alertState by mutableStateOf(false)
    private set
  fun changeAlertState() { alertState = !alertState }


  var alertStateDeleteId by mutableStateOf<Long>(0)
    private set

  fun changeAlertStateDeleteId(id: Long){
    alertStateDeleteId = id
  }

  var alertStateDeleteString by mutableStateOf("")
    private set

  fun changeAlertStateDeleteString(value: String){
    alertStateDeleteString = value
  }

  var balance by mutableStateOf<Long>(0)
    private set
  fun changeBalance(data: List<Data>) {
    balance = Utils.totalBalance(data)
  }

  var balanceSwitch by mutableStateOf(false)
    private set
  fun changeBalanceSwitch(value: Boolean = balanceSwitch){
    balanceSwitch = !value
  }
}