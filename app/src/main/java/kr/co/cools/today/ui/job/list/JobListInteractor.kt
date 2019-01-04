package kr.co.cools.today.ui.job.list

import android.arch.lifecycle.LiveData
import kr.co.cools.today.repo.dao.JobDao
import kr.co.cools.today.repo.entities.JobEntity
import javax.inject.Inject

class JobListInteractor @Inject constructor(val jobDao: JobDao) {

    fun getJobAll(): LiveData<List<JobEntity>> {
        return jobDao.getAllLiveData()
    }
}