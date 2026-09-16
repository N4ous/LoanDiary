package com.ajshahariar.loandiary.di

import android.content.Context
import androidx.room.Room
import com.ajshahariar.loandiary.data.AppDatabase
import com.ajshahariar.loandiary.data.LoanDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "loan_diary_database"
        ).build()
    }

    @Provides
    fun provideLoanDao(database: AppDatabase): LoanDao {
        return database.loanDao()
    }
}
