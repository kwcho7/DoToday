package kr.co.cools.today.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kr.co.cools.today.TodayContext
import kr.co.cools.today.repo.TodayRoomDatabase
import kr.co.cools.today.repo.dao.JobDao
import kr.co.cools.today.repo.dao.TodoDao
import kr.co.cools.today.todayContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class TodayAppModule {


    @Singleton
    @Provides
    fun todayRoomDatabase(@ApplicationContext context: Context) : TodayRoomDatabase {
        return Room.databaseBuilder(
            context,
            TodayRoomDatabase::class.java,
            "do-today"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun todoDao(todayRoomDatabase: TodayRoomDatabase) : TodoDao {
        return todayRoomDatabase.todoDao()
    }

    @Singleton
    @Provides
    fun jobDao(todayRoomDatabase: TodayRoomDatabase) : JobDao {
        return todayRoomDatabase.jobDao()
    }

    @Singleton
    @Provides
    fun todayContext(@ApplicationContext context: Context) : TodayContext {
        return context.todayContext()
    }

}