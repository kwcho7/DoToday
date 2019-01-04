package kr.co.cools.today.ui.job.list

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import kr.co.cools.today.repo.entities.JobEntity
import kr.co.cools.today.ui.BaseViewModel
import javax.inject.Inject

class JobListViewModel @Inject constructor(val interactor: JobListInteractor): BaseViewModel<JobListViewModel.JobListViewState>() {

    private val jobList = interactor.getJobAll()

    override fun onCleared() {
        super.onCleared()
    }

    override fun observer(lifecycleOwner: LifecycleOwner, observer: Observer<JobListViewState>) {
        super.observer(lifecycleOwner, observer)
        jobList.observe(lifecycleOwner, Observer {
            it?.let {
                viewModelState.value = JobListViewState.UpdateJobList(it)
            }
        })
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner) {
        super.removeObservers(lifecycleOwner)
    }

    override fun dispatch(action: ViewModelAction) {

    }

    sealed class JobListViewState: ViewModelState{
        data class UpdateJobList(val jobList: List<JobEntity>): JobListViewState()
    }
}