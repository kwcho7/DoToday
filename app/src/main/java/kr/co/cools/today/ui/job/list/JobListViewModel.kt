package kr.co.cools.today.ui.job.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import io.reactivex.Single
import kr.co.cools.common.extension.asDriver
import kr.co.cools.common.extension.disposableBag
import kr.co.cools.today.TodayContext
import kr.co.cools.today.repo.entities.JobEntity
import kr.co.cools.today.ui.BaseViewModel
import kr.co.cools.today.ui.utils.WeekNumber

class JobListViewModel @ViewModelInject constructor(private val todayContext: TodayContext, val interactor: JobListInteractor): BaseViewModel<JobListViewModel.JobListViewState>() {
    private val jobList = interactor.getTodayJobAllLive(false)
    private val doneJobList = interactor.getTodayJobAllLive(true)

    override fun observer(lifecycleOwner: LifecycleOwner, observer: Observer<JobListViewState>) {
        super.observer(lifecycleOwner, observer)
        jobList.observe(lifecycleOwner, Observer {
            it?.let {
                notifyChangeViewState(JobListViewState.UpdateJobList(it))
            }
        })
        doneJobList.observe(lifecycleOwner, Observer {
            it?.let {
                notifyChangeViewState(JobListViewState.UpdateDoneJobList(it))
            }
        })
        updateDayOfWeek()
    }

    private fun updateDayOfWeek() {
        notifyChangeViewState(JobListViewState.UpdateDayOfWeek(WeekNumber.getWeekString(todayContext.getContext())))
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner) {
        super.removeObservers(lifecycleOwner)
        jobList.removeObservers(lifecycleOwner)
        doneJobList.removeObservers(lifecycleOwner)
    }

    override fun dispatch(action: ViewModelAction) {

    }

    fun updateCompleteJob(jobEntity: JobEntity) {
        Single.create<Boolean> {
            interactor.updateComplete(jobEntity)
            it.onSuccess(true)
        }
        .asDriver()
        .doOnSuccess {  }
        .subscribe()
            .disposableBag(compositeDisposable)
    }

    sealed class JobDispatch: ViewModelAction{
    }

    sealed class JobListViewState: ViewModelState{
        data class UpdateJobList(val jobList: List<JobEntity>): JobListViewState()
        data class UpdateDoneJobList(val jobList: List<JobEntity>): JobListViewState()
        data class UpdateDayOfWeek(val dayOfWeek: String): JobListViewState()
    }
}