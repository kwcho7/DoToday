package kr.co.cools.today.di

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import kr.co.cools.today.TodayApplication
import kr.co.cools.today.repo.TodayRepository
import kr.co.cools.today.repo.TodayRoomDatabase
import kr.co.cools.today.repo.dao.JobDao
import kr.co.cools.today.repo.dao.TodoDao
import javax.inject.Singleton

@Module
class TodayAppModule {

    @Provides
    fun context(application: TodayApplication): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun todayRepository(context: Context, todoDao: TodoDao, jobDao: JobDao): TodayRepository {
        return TodayRepository(context, todoDao, jobDao)
    }

    @Singleton
    @Provides
    fun todayRoomDatabase(context: Context) : TodayRoomDatabase {
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
}