package com.nursyah.finance.db

import androidx.room.*
import com.nursyah.finance.db.model.Data
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDAO {
  @Query("SELECT * FROM DATA_TABLE")
  fun getAllData(): Flow<List<Data>>
  @Query("SELECT * FROM DATA_TABLE WHERE :category = category")
  fun getDataByCategory(category: String): Flow<List<Data>>

  @Query("SELECT * FROM DATA_TABLE WHERE :date = date")
  fun getDataByDate(date: String): Flow<List<Data>>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(data: Data)

  @Query("DELETE FROM DATA_TABLE")
  fun deleteAll()

  @Query("DELETE FROM DATA_TABLE WHERE :id = id")
  fun deleteById(id: Long)
}