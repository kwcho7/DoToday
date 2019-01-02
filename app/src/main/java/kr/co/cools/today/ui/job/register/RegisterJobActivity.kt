package kr.co.cools.today.ui.job.register

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import kr.co.cools.common.logger.Logger
import kr.co.cools.today.di.ViewModelFactory
import kr.co.cools.today.repo.entities.JobEntity
import javax.inject.Inject

class RegisterJobActivity: DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: RegisterJobViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterJobViewModel::class.java)
        viewModel.viewModelState.observe(this, Observer {
            it?.apply {
                changeViewState(this)
            }
        })

        viewModel.jobListLiveData.observe(this, Observer {
            it?.apply {
                onChangeJobList(this)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.viewModelState.removeObservers(this)
    }

    private fun onChangeJobList(list: List<JobEntity>) {
        Logger.v("onChangeJobList.${list}")
    }

    private fun changeViewState(viewState: RegisterJobViewModel.RegisterJobViewState){
        Logger.v("changeViewState.${viewState}")
    }
}
