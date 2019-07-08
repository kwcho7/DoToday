package kr.co.cools.today.ui.launcher

import android.util.Log
import kr.co.cools.common.extension.asDriver
import kr.co.cools.common.extension.disposableBag
import kr.co.cools.today.ui.BaseViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LauncherViewModel @Inject constructor(val interactor: LauncherInteractor): BaseViewModel<LauncherViewModel.LauncherState>() {

    init {
        notifyChangeViewState(LauncherState.Idle)
    }

    override fun dispatch(action: ViewModelAction) {
    }

    /**
     * LauncherState 를 업데이트한다.
     * [#launcherState] 을 observe 하여 변경될때 UI 를 업데이트 시켜줘야한다.
     */
    fun updateLauncherState() {
        notifyChangeViewState(LauncherState.ShowProgress)
        interactor.hasTodoEntity()
            .delay(20, TimeUnit.MILLISECONDS)
            .asDriver()
            .subscribe(
                {
                    notifyChangeViewState(LauncherState.HideProgress)
                    if(it){
                        notifyChangeViewState(LauncherState.StartJobListActivity)
                    }else {
                        notifyChangeViewState(LauncherState.StartTodoListActivity)
                    }
                },
                {
                    notifyChangeViewState(LauncherState.HideProgress)
                    notifyChangeViewState(LauncherState.ErrorMessage(Log.getStackTraceString(it)))
                }
            ).disposableBag(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
    }

    sealed class LauncherState: ViewModelState{
        /**
         * 초기 상태
         */
        object Idle: LauncherState()

        /**
         * Progress 를 보여준다.
         */
        object ShowProgress: LauncherState()

        /**
         * Progress 를 숨긴다.
         */
        object HideProgress: LauncherState()

        /**
         *
         */
        object StartTodoListActivity: LauncherState()

        /**
         *
         */
        object StartJobListActivity: LauncherState()

        /**
         * 오류 발생시 메세지를 보여준다.
         */
        data class ErrorMessage(var message: String): LauncherState()
    }
}