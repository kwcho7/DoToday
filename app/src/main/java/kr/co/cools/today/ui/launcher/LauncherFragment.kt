package kr.co.cools.today.ui.launcher

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fargment_launcher.*
import kr.co.cools.common.extension.gone
import kr.co.cools.common.extension.visible
import kr.co.cools.today.R
import kr.co.cools.today.ui.home.HomeActivity

@Keep
@AndroidEntryPoint
class LauncherFragment: Fragment() {

    private val launcherViewModel: LauncherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fargment_launcher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        savedInstanceState ?: launcherViewModel.updateLauncherState()
        launcherViewModel.observer(this, Observer {
            when(it){
                LauncherViewModel.LauncherState.Idle -> {
                }
                LauncherViewModel.LauncherState.ShowProgress -> {
                    progressBar.visible()
                }
                LauncherViewModel.LauncherState.HideProgress -> {
                    progressBar.gone()
                }
                LauncherViewModel.LauncherState.launchTodoList -> {
                    startActivity(Intent(context, HomeActivity::class.java))
                }
                LauncherViewModel.LauncherState.launchJobList -> {
                    startActivity(Intent(context, HomeActivity::class.java))
                }
                is LauncherViewModel.LauncherState.ErrorMessage -> {
                    TODO("not implementation")
                }
            }
        })
    }
}