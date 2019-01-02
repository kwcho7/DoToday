package kr.co.cools.today.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import kr.co.cools.today.ui.job.list.JobListActivity
import kr.co.cools.today.ui.launcher.LauncherActivity
import kr.co.cools.today.ui.todo.list.TodoListActivity
import kr.co.cools.today.ui.todo.register.RegisterTodoActivity

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun launcherActivity(): LauncherActivity

    @ContributesAndroidInjector
    abstract fun todoLiatActivity(): TodoListActivity

    @ContributesAndroidInjector
    abstract fun registerTodoActivity(): RegisterTodoActivity

    @ContributesAndroidInjector
    abstract fun jobListActivity(): JobListActivity
}