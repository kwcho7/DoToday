package kr.co.cools.today.ui.job.register

import kr.co.cools.today.ui.BaseViewModel
import javax.inject.Inject

class RegisterJobViewModel @Inject constructor(val interactor: RegisterJobInteractor): BaseViewModel<RegisterJobViewModel.RegisterJobViewState>() {

    val jobListLiveData = interactor.requestJobList()

    override fun dispatch(action: ViewModelAction){
    }

    sealed class RegisterJobViewState: ViewModelState{
        data class UpdateProgress(val isShow: Boolean): RegisterJobViewState()
    }

    class RegisterJobAction: ViewModelAction{
    }
}