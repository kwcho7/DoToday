package kr.co.cools.today

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import kr.co.cools.common.logger.Logger
import kr.co.cools.today.di.DaggerTodayComponent
import timber.log.Timber


class TodayApplication: DaggerApplication(){
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTodayComponent.builder().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        Logger.init(BuildConfig.DEBUG, "DoIt")
    }
}