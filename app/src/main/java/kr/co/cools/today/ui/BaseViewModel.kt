package kr.co.cools.today.ui

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import kr.co.cools.today.ui.utils.SingleLiveData

abstract class BaseViewModel<T: BaseViewModel.ViewModelState>: ViewModel() {
    protected val compositeDisposable = CompositeDisposable()
    private val viewModelState = SingleLiveData<T>()

    abstract fun dispatch(action: ViewModelAction)

    protected fun notifyChangeViewState(viewState: T){
        viewModelState.value = viewState
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    open fun observer(lifecycleOwner: LifecycleOwner, observer: Observer<T>){
        viewModelState.observe(lifecycleOwner, observer)
    }

    open fun removeObservers(lifecycleOwner: LifecycleOwner){
        viewModelState.removeObservers(lifecycleOwner)
    }

    fun observeForever(observer: Observer<T>) {
        viewModelState.observeForever(observer)
    }

    interface ViewModelState
    interface StickyViewModelState:ViewModelState

    interface ViewModelAction
}