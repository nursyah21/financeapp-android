package com.nursyah.finance.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "DATA_TABLE")
data class Data(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val date: String = LocalDate.now().toString(),
  val category: String,
  val item: String = category,
  val value: Long
)