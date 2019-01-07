package kr.co.cools.today.ui.job.list

import android.arch.lifecycle.LiveData
import io.reactivex.Single
import kr.co.cools.today.repo.dao.JobDao
import kr.co.cools.today.repo.entities.JobEntity
import kr.co.cools.today.ui.utils.DateUtils
import javax.inject.Inject

class JobListInteractor @Inject constructor(private val jobDao: JobDao) {

    fun getTodayJobAllLive(completed: Boolean): LiveData<List<JobEntity>> {
        return jobDao.getAllLiveData(DateUtils.getCurrentDate(), completed)
    }
}