package com.nursyah.finance.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nursyah.finance.db.model.Data

@Database(entities = [Data::class], version = 2, exportSchema = false)
abstract class DataDb : RoomDatabase(){
  abstract fun dataDao(): DataDAO

}