package com.nursyah.finance.model

data class Time(
  val Date: Int,
  val Month: Int,
  val Year: Int,
){
  override fun toString(): String {
    return "$Date-$Month-$Year"
  }
}

data class Data(
  val key: String,
  val value: Any
)

data class Spending(
  val Time: Time,
  val Name: String,
  val Value: Int
)

data class Income(
  val Time: Time,
  val Name: String,
  val Value: Int
)

data class Pocket(
  val Name: String,
  val Value: Int
)

data class Saving(
  val Name: String,
  val Value: Int,
  val Target: Time
)
