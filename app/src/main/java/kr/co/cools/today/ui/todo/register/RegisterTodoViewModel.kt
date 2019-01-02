package kr.co.cools.today.ui.todo.register

import android.text.TextUtils
import io.reactivex.disposables.CompositeDisposable
import kr.co.cools.common.extension.disposableBag
import kr.co.cools.today.ui.BaseViewModel
import javax.inject.Inject

class RegisterTodoViewModel @Inject constructor(val interactor: RegisterTodoInteractor): BaseViewModel<RegisterTodoViewModel.ViewState>() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    override fun dispatch(action: BaseViewModel.ViewModelAction) {
        viewModelState.value = ViewState.ProgressViewState(true)
        when(action){
            is Action.CompleteAction -> {
                if(TextUtils.isEmpty(action.title)){
                    viewModelState.value = ViewState.ProgressViewState(false)
                    viewModelState.value = ViewState.EmptyTitleViewState
                    return
                }
                interactor.addTodoEntity(action.title, action.description, action.dayOfWeekNumber)
                    .subscribe(
                        {
                            viewModelState.value = ViewState.ProgressViewState(false)
                            viewModelState.value = ViewState.AddCompleteViewState
                        },
                        {
                            viewModelState.value = ViewState.ProgressViewState(false)
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

    sealed class Action: BaseViewModel.ViewModelAction{
        data class CompleteAction(val title: String, val description: String, val dayOfWeekNumber: Int): Action()
    }
}