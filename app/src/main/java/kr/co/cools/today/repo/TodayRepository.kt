package kr.co.cools.today.repo

import android.content.Context
import io.reactivex.Single
import kr.co.cools.today.repo.dao.JobDao
import kr.co.cools.today.repo.dao.TodoDao
import kr.co.cools.today.repo.entities.JobEntity
import kr.co.cools.today.repo.entities.TodoEntity
import javax.inject.Inject

class TodayRepository @Inject constructor(val context: Context, val todoDao: TodoDao, val jobDao: JobDao) {

    fun getAll() : Single<List<TodoEntity>>{
        return todoDao.getAll()
    }

    fun getJobs(): Single<List<JobEntity>> {
        return jobDao.getAll()
    }
}

