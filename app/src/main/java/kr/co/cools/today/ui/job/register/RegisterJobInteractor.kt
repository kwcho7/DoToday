package kr.co.cools.today.ui.job.register

import android.arch.lifecycle.LiveData
import io.reactivex.Single
import kr.co.cools.today.repo.dao.JobDao
import kr.co.cools.today.repo.entities.JobEntity
import javax.inject.Inject

class RegisterJobInteractor @Inject constructor(val jobDao: JobDao) {
    fun requestJobList(): LiveData<List<JobEntity>> {
        return jobDao.getAllLiveData()
    }
}