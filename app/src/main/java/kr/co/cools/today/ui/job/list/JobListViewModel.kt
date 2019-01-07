package kr.co.cools.today.ui.job.list

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kr.co.cools.common.extension.disposableBag
import kr.co.cools.today.repo.entities.DayOfWeek
import kr.co.cools.today.repo.entities.JobEntity
import kr.co.cools.today.ui.BaseViewModel
import kr.co.cools.today.ui.utils.WeekNumber
import java.util.*
import javax.inject.Inject

class JobListViewModel @Inject constructor(val context: Context, val interactor: JobListInteractor): BaseViewModel<JobListViewModel.JobListViewState>() {
    private val compositeDisposable = CompositeDisposable()
    private val jobList = interactor.getTodayJobAll()

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
        updateDayOfWeek()
    }

    private fun updateDayOfWeek() {
        viewModelState.value = JobListViewState.UpdateDayOfWeek(WeekNumber.getWeekString(context))
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner) {
        super.removeObservers(lifecycleOwner)
        jobList.removeObservers(lifecycleOwner)
    }

    override fun dispatch(action: ViewModelAction) {

    }

    sealed class JobListViewState: ViewModelState{
        data class UpdateJobList(val jobList: List<JobEntity>): JobListViewState()
        data class UpdateDayOfWeek(val dayOfWeek: String): JobListViewState()
    }
}