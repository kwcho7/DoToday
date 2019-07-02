package kr.co.cools.today.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kr.co.cools.today.ui.job.list.JobListViewModel
import kr.co.cools.today.ui.launcher.LauncherViewModel
import kr.co.cools.today.ui.todo.list.TodoListViewModel
import kr.co.cools.today.ui.todo.register.RegisterTodoViewModel
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LauncherViewModel::class)
    internal abstract fun mainViewModel(viewModel: LauncherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodoListViewModel::class)
    internal abstract fun todoListViewModel(viewModel: TodoListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterTodoViewModel::class)
    internal abstract fun registerTodoViewModel(viewModel: RegisterTodoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobListViewModel::class)
    internal abstract fun jobListViewModel(viewModel: JobListViewModel): ViewModel
}


@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)