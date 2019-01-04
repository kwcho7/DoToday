package kr.co.cools.today.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.work.*
import dagger.android.DaggerService
import kr.co.cools.common.logger.Logger
import kr.co.cools.today.repo.entities.JobEntity
import kr.co.cools.today.todayContext
import kr.co.cools.today.ui.utils.DateUtils
import kr.co.cools.today.ui.utils.WeekNumber
import java.util.concurrent.TimeUnit

class JobRegisterService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val jobWorker = PeriodicWorkRequest.Builder(JobWorker::class.java, 15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork("JOB", ExistingPeriodicWorkPolicy.REPLACE, jobWorker)
        return Service.START_STICKY
    }

    class JobWorker(val context: Context, params: WorkerParameters): Worker(context, params){
        override fun doWork(): Result {
            val jobDao = context.todayContext().jobDao()
            val todoDao = context.todayContext().todoDao()

            val todayString = DateUtils.getCurrentDate()
            val jobList = jobDao.getAll(todayString).blockingGet()
            if(jobList.isEmpty()){
                val todoList = todoDao.getAll(WeekNumber.getNumber()).blockingGet()
                todoList.forEach {
                    jobDao.insert(
                        JobEntity(
                            date = todayString,
                            todo = it
                        )
                    )
                }
            }
            Logger.v("doWork.${jobList}")
            return Result.success()
        }
    }
}


