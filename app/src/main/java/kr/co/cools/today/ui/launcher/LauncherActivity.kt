package kr.co.cools.today.ui.launcher

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_launcher.*
import kr.co.cools.common.extension.gone
import kr.co.cools.common.extension.visible
import kr.co.cools.common.logger.Logger
import kr.co.cools.today.R
import kr.co.cools.today.ui.job.list.JobListActivity
import kr.co.cools.today.ui.todo.list.TodoListActivity
import javax.inject.Inject

class LauncherActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: LauncherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LauncherViewModel::class.java)
        viewModel.launcherState.observe(this, LauncherStateObserver())
        viewModel.updateLauncherState()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.launcherState.removeObservers(this)
    }

    inner class LauncherStateObserver: Observer<LauncherViewModel.LauncherState> {
        override fun onChanged(launcherState: LauncherViewModel.LauncherState?) {
            launcherState?.let { onViewStateChanged(it) }
        }

        private fun onViewStateChanged(launcherState: LauncherViewModel.LauncherState) =
            when(launcherState){
                LauncherViewModel.LauncherState.Idle -> {}
                LauncherViewModel.LauncherState.ShowProgress -> {
                    progressBar.visible()
                }
                LauncherViewModel.LauncherState.HideProgress -> {
                    progressBar.gone()
                }
                LauncherViewModel.LauncherState.StartTodoListActivity -> {
                    startActivity(Intent(this@LauncherActivity, TodoListActivity::class.java))
                }
                LauncherViewModel.LauncherState.StartJobListActivity -> {
                    startActivity(Intent(this@LauncherActivity, JobListActivity::class.java))
                }
                is LauncherViewModel.LauncherState.ErrorMessage -> {
                    TODO("not implementation")
                }
            }
    }
}
