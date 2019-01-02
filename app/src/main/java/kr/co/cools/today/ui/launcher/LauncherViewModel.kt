package kr.co.cools.today.ui.launcher

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kr.co.cools.common.extension.asDriver
import kr.co.cools.common.extension.disposableBag
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LauncherViewModel @Inject constructor(val context: Context, val interactor: LauncherInteractor): ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val launcherState = MutableLiveData<LauncherState>().apply {
        postValue(LauncherState.Idle)
    }

    /**
     * LauncherState 를 업데이트한다.
     * [#launcherState] 을 observe 하여 변경될때 UI 를 업데이트 시켜줘야한다.
     */
    fun updateLauncherState() {
        launcherState.value = LauncherState.ShowProgress
        interactor.hasTodoEntity()
            .delay(20, TimeUnit.MILLISECONDS)
            .asDriver()
            .subscribe(
                {
                    launcherState.value = LauncherState.HideProgress
                    launcherState.value = LauncherState.StartRegisterJobActivity

//                    if(it){
//                        launcherState.value = LauncherState.StartRegisterJobActivity
//                    }
//                    else {
//                        launcherState.value = LauncherState.StartTodoListActivity
//                    }
                },
                {
                    launcherState.value = LauncherState.HideProgress
                    launcherState.value = LauncherState.ErrorMessage(
                        Log.getStackTraceString(it)
                    )
                }
            ).disposableBag(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    sealed class LauncherState{
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
         * 오늘 할일 Activity 를 실행한다.
         */
        object StartTodoListActivity: LauncherState()

        /**
         * 할일 등록 Activity 를 실행한다.
         */
        object StartRegisterJobActivity: LauncherState()

        /**
         * 오류 발생시 메세지를 보여준다.
         */
        data class ErrorMessage(var message: String): LauncherState()
    }
}