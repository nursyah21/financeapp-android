package com.nursyah.finance.model

data class Time(
  val Date: Int,
  val Month: Int,
  val Year: Int,
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
