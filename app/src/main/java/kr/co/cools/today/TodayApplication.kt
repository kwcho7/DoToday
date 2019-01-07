package kr.co.cools.today

import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import kr.co.cools.common.logger.Logger
import kr.co.cools.today.di.DaggerTodayComponent
import kr.co.cools.today.repo.dao.JobDao
import kr.co.cools.today.repo.dao.TodoDao
import kr.co.cools.today.service.JobRegisterService
import javax.inject.Inject


class TodayApplication: DaggerApplication(), TodayContext{
    @Inject
    lateinit var jobDao: JobDao

    @Inject
    lateinit var todoDao: TodoDao

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTodayComponent.builder().create(this)
    }

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

}

fun Context.todayContext() : TodayContext {
    return applicationContext as TodayContext
}

interface TodayContext{
    fun jobDao(): JobDao
    fun todoDao(): TodoDao
}