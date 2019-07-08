package kr.co.cools.today.ui.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kr.co.cools.today.ui.BaseViewModel

class SingleLiveData<T: BaseViewModel.ViewModelState>: Observer<T> {

    private var observer: Observer<T>? = null
    private val liveData = MutableLiveData<T>()

    var value:T?
        set(value) {
            liveData.value = value
        }
        get() {
            return liveData.value
        }

    override fun onChanged(t: T?) {
        t?.let {
            observer?.onChanged(t)
            if(t !is BaseViewModel.StickyViewModelState){
                liveData.value = null
            }
        }
    }

    fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        this.observer = observer
        liveData.observe(owner, this)
    }

    fun observeForever(observer: Observer<T>) {
        this.observer = observer
        liveData.observeForever(observer)
    }

    fun removeObservers(owner: LifecycleOwner) {
        liveData.removeObservers(owner)
    }
}