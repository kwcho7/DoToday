package kr.co.cools.today.ui.launcher

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_launcher.*
import kr.co.cools.common.extension.gone
import kr.co.cools.common.extension.visible
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
            launcherState?.also { state ->
                when(state){
                    is LauncherViewModel.LauncherState.Idle -> {
                    }
                    is LauncherViewModel.LauncherState.ShowProgress -> {
                        progressBar.visible()
                    }
                    is LauncherViewModel.LauncherState.HideProgress -> {
                        progressBar.gone()
                    }
                    is LauncherViewModel.LauncherState.StartTodoListActivity -> {
                        startActivity(Intent(this@LauncherActivity, TodoListActivity::class.java))
                    }
                    is LauncherViewModel.LauncherState.StartJobListActivity -> {
                        startActivity(Intent(this@LauncherActivity, JobListActivity::class.java))
                    }
                }
            }
        }
    }
}
