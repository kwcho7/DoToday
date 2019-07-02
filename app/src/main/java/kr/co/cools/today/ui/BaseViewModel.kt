package kr.co.cools.today.ui

import androidx.lifecycle.*

abstract class BaseViewModel<T: BaseViewModel.ViewModelState>: ViewModel() {

    val viewModelState = MutableLiveData<T>()

    abstract fun dispatch(action: ViewModelAction)

    open fun observer(lifecycleOwner: LifecycleOwner, observer: Observer<T>){
        viewModelState.observe(lifecycleOwner, observer)
    }

    open fun removeObservers(lifecycleOwner: LifecycleOwner){
        viewModelState.removeObservers(lifecycleOwner)
    }

    interface ViewModelState
    interface ViewModelAction
}