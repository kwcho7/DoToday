package kr.co.cools.today.ui.todo.register

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.widget.SimpleAdapter
import android.widget.SpinnerAdapter
import dagger.android.support.DaggerAppCompatActivity
import kr.co.cools.today.R
import kr.co.cools.today.di.ViewModelFactory
import javax.inject.Inject

class RegisterTodoActivity: DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: RegisterTodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_todo)
        initViewModel()
        initDayOfWeekSpinner()
        initButtonAction()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterTodoViewModel::class.java)
    }

    private fun initDayOfWeekSpinner() {

    }

    private fun initButtonAction() {

    }

}