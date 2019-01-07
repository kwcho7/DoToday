package kr.co.cools.today.ui.job.list

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kr.co.cools.common.extension.asDriver
import kr.co.cools.common.extension.disposableBag
import kr.co.cools.today.TodayApplication
import kr.co.cools.today.TodayContext
import kr.co.cools.today.repo.entities.DayOfWeek
import kr.co.cools.today.repo.entities.JobEntity
import kr.co.cools.today.ui.BaseViewModel
import kr.co.cools.today.ui.utils.WeekNumber
import java.util.*
import javax.inject.Inject

class JobListViewModel @Inject constructor(private val todayContext: TodayContext, interactor: JobListInteractor): BaseViewModel<JobListViewModel.JobListViewState>() {
    private val compositeDisposable = CompositeDisposable()

    private val jobList = interactor.getTodayJobAllLive(false)
    private val doneJobList = interactor.getTodayJobAllLive(true)


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    override fun observer(lifecycleOwner: LifecycleOwner, observer: Observer<JobListViewState>) {
        super.observer(lifecycleOwner, observer)
        jobList.observe(lifecycleOwner, Observer {
            it?.let {
                viewModelState.value = JobListViewState.UpdateJobList(it)
            }
        })
        doneJobList.observe(lifecycleOwner, Observer {
            it?.let {
                viewModelState.value = JobListViewState.UpdateDoneJobList(it)
            }
        })
        updateDayOfWeek()
    }

    private fun updateDayOfWeek() {
        viewModelState.value = JobListViewState.UpdateDayOfWeek(WeekNumber.getWeekString(todayContext.getContext()))
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner) {
        super.removeObservers(lifecycleOwner)
        jobList.removeObservers(lifecycleOwner)
        doneJobList.removeObservers(lifecycleOwner)
    }

    override fun dispatch(action: ViewModelAction) {

    }

    sealed class JobDispatch: ViewModelAction{
    }

    sealed class JobListViewState: ViewModelState{
        data class UpdateJobList(val jobList: List<JobEntity>): JobListViewState()
        data class UpdateDoneJobList(val jobList: List<JobEntity>): JobListViewState()
        data class UpdateDayOfWeek(val dayOfWeek: String): JobListViewState()
    }
}