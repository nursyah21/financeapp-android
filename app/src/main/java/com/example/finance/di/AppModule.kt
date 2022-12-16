package com.example.finance.di

import android.content.Context
import androidx.room.Room
import com.example.finance.R
import com.example.finance.db.DataDAO
import com.example.finance.db.DataDb
import com.example.finance.db.DataRepositoryImpl
import com.example.finance.db.model.DataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
  @Provides
  fun provideBookDb(@ApplicationContext context: Context) =
    Room.databaseBuilder(context, DataDb::class.java, context.getString(R.string.database_name))
      .fallbackToDestructiveMigration()
      .build()
  @Provides
  fun provideBookDao(dataDb: DataDb) = dataDb.dataDao()
  @Provides
  fun provideDataRepository(dataDAO: DataDAO):DataRepository =
    DataRepositoryImpl(dataDAO)
}