package kr.co.cools.today.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import kr.co.cools.today.TodayApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        TodayAppModule::class,
        ViewModelModule::class,
        ActivityBindingModule::class
    ]
)
interface TodayComponent: AndroidInjector<TodayApplication> {

    @Component.Builder
    abstract class Builder: AndroidInjector.Builder<TodayApplication>()

}