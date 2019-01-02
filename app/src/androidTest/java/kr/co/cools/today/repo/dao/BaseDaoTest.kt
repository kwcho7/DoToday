package kr.co.cools.today.repo.dao

import android.content.Context
import android.support.test.InstrumentationRegistry
import dagger.Component
import dagger.Module
import dagger.Provides
import kr.co.cools.today.TodayApplication
import kr.co.cools.today.di.TodayAppModule
import org.junit.Before
import javax.inject.Singleton


@Singleton
@Component(
    modules = [ApplicationModule::class, TodayAppModule::class]
)
interface RepoComponent {
    fun todoDao() : TodoDao
    fun jobDao(): JobDao
}


@Module
class ApplicationModule(val context: Context) {
    @Provides
    fun app(): TodayApplication {
        return context.applicationContext as TodayApplication
    }
}


abstract class BaseDaoTest {
    lateinit var component: RepoComponent

    abstract fun moduleSetup(component: RepoComponent)

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getTargetContext()
        component = DaggerRepoComponent.builder()
            .todayAppModule(TodayAppModule())
            .applicationModule(ApplicationModule(context))
            .build()
        moduleSetup(component)
    }
}