package kr.co.cools.today

import android.app.Application
import android.content.Context
import android.content.Intent
import dagger.hilt.android.HiltAndroidApp
import kr.co.cools.common.logger.Logger
import kr.co.cools.today.repo.dao.JobDao
import kr.co.cools.today.repo.dao.TodoDao
import kr.co.cools.today.service.JobRegisterService
import javax.inject.Inject

@HiltAndroidApp
class TodayApplication: Application(), TodayContext{

    @Inject
    lateinit var jobDao: JobDao

    @Inject
    lateinit var todoDao: TodoDao

    override fun onCreate() {
        super.onCreate()
        Logger.init(BuildConfig.DEBUG, "DoIt")
        startService(Intent(this, JobRegisterService::class.java))
    }

    override fun jobDao(): JobDao {
        return jobDao
    }

    override fun todoDao(): TodoDao {
        return todoDao
    }

    override fun getContext(): Context {
        return this
    }
}

fun Context.todayContext() : TodayContext {
    return applicationContext as TodayContext
}

interface TodayContext{
    fun jobDao(): JobDao
    fun todoDao(): TodoDao
    fun getContext(): Context
}