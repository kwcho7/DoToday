package kr.co.cools.today.ui.todo.register

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import kr.co.cools.common.extension.disposableBag
import kr.co.cools.today.ui.BaseViewModel

class RegisterTodoViewModel @ViewModelInject constructor(val interactor: RegisterTodoInteractor): BaseViewModel<RegisterTodoViewModel.ViewState>() {

    override fun dispatch(action: ViewModelAction) {
        notifyChangeViewState(ViewState.ProgressViewState(true))
        when(action){
            is Action.CompleteAction -> {
                if(TextUtils.isEmpty(action.title)){
                    notifyChangeViewState(ViewState.ProgressViewState(false))
                    notifyChangeViewState(ViewState.EmptyTitleViewState)
                    return
                }
                interactor.addTodoEntity(action.title, action.description, action.dayOfWeekNumber)
                    .subscribe(
                        {
                            notifyChangeViewState(ViewState.ProgressViewState(false))
                            notifyChangeViewState(ViewState.AddCompleteViewState)
                        },
                        {
                            notifyChangeViewState(ViewState.ProgressViewState(false))
                        }
                    ).disposableBag(compositeDisposable)
            }
        }
    }

    sealed class ViewState: ViewModelState{
        data class ProgressViewState(val isShow:Boolean): ViewState()
        object AddCompleteViewState: ViewState()
        object EmptyTitleViewState: ViewState()
    }

    sealed class Action: ViewModelAction{
        data class CompleteAction(val title: String, val description: String, val dayOfWeekNumber: Int): Action()
    }
}