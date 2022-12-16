package com.example.finance.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.finance.db.model.Data

@Database(entities = [Data::class], version = 2, exportSchema = false)
abstract class DataDb : RoomDatabase(){
  abstract fun dataDao(): DataDAO

}