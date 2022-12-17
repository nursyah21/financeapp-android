package com.nursyah.finance.di

import android.content.Context
import androidx.room.Room
import com.nursyah.finance.R
import com.nursyah.finance.db.DataDAO
import com.nursyah.finance.db.DataDb
import com.nursyah.finance.db.DataRepositoryImpl
import com.nursyah.finance.db.model.DataRepository
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
  fun provideDataRepository(dataDAO: DataDAO): DataRepository =
    DataRepositoryImpl(dataDAO)
}