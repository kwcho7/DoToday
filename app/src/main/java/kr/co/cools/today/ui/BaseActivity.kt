package kr.co.cools.today.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

abstract class BaseActivity<VM: BaseViewModel<VS>, VS: BaseViewModel.ViewModelState>: AppCompatActivity() {

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