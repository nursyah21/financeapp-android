package com.nursyah.finance.db

import com.nursyah.finance.db.model.Data
import com.nursyah.finance.db.model.DataRepository
import kotlinx.coroutines.flow.Flow

class DataRepositoryImpl (private val dataDAO: DataDAO): DataRepository{
  override fun getAllData(): Flow<List<Data>> = dataDAO.getAllData()

  override fun getDataByCategory(category: String): Flow<List<Data>> =
    dataDAO.getDataByCategory(category)

  override fun getDataByDate(date: String): Flow<List<Data>> =
    dataDAO.getDataByDate(date)

  override fun addData(data: Data) = dataDAO.insert(data)

  override fun deleteAllData() = dataDAO.deleteAll()

  override fun deleteById(id: Long) = dataDAO.deleteById(id)
}