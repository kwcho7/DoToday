package kr.co.cools.today.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import kr.co.cools.today.ui.launcher.LauncherActivity
import kr.co.cools.today.ui.register.job.RegisterJobActivity

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun launcherActivity(): LauncherActivity

    @ContributesAndroidInjector
    abstract fun registerJobActivity(): RegisterJobActivity
}