package kr.co.cools.today.ui.launcher

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.android.synthetic.main.activity_launcher.*
import kr.co.cools.common.extension.gone
import kr.co.cools.common.extension.visible
import kr.co.cools.common.logger.Logger
import kr.co.cools.today.R
import kr.co.cools.today.ui.BaseActivity
import kr.co.cools.today.ui.job.list.JobListActivity
import kr.co.cools.today.ui.todo.list.TodoListActivity
import javax.inject.Inject

@AndroidEntryPoint
class LauncherActivity : BaseActivity<LauncherViewModel, LauncherViewModel.LauncherState>() {

    override fun createViewModel(): LauncherViewModel {
        return ViewModelProvider(this).get(LauncherViewModel::class.java)
    }

    @Inject lateinit var injector: LauncherInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        viewModel.updateLauncherState()
    }

    override fun onViewStateChanged(viewState: LauncherViewModel.LauncherState) = when (viewState) {
        LauncherViewModel.LauncherState.Idle -> {
        }
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
