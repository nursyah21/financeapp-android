package com.example.finance.db.model

import kotlinx.coroutines.flow.Flow


interface DataRepository{
  fun getAllData(): Flow<List<Data>>
  fun getDataByCategory(category: String): Flow<List<Data>>
  fun getDataByDate(date: String): Flow<List<Data>>
  fun addData(data: Data)
  fun deleteAllData()
  fun deleteById(id: Long)
}