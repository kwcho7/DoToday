package kr.co.cools.today.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<VM: BaseViewModel<VS>, VS: BaseViewModel.ViewModelState>: DaggerAppCompatActivity() {

    protected lateinit var viewModel: VM

    protected abstract fun createViewModel(): VM

    abstract fun onViewStateChanged(viewState: VS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
        viewModel.observer(this, Observer {
            it?.let { onViewStateChanged(it) }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeObservers(this)
    }
}