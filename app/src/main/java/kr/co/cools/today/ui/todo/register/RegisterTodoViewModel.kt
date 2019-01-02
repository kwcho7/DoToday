package kr.co.cools.today.ui.todo.register

import kr.co.cools.today.ui.BaseViewModel
import javax.inject.Inject

class RegisterTodoViewModel @Inject constructor(val interactor: RegisterTodoInteractor): BaseViewModel<RegisterTodoViewModel.ViewState>() {

    override fun dispatch(action: BaseViewModel.ViewModelAction) {
    }

    sealed class ViewState: ViewModelState{

    }

    sealed class Action: BaseViewModel.ViewModelAction{
        data class CompleteAction(val title: String, val description: String, val dayOfWeekNumber: Int)
    }
}