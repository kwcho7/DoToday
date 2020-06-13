package kr.co.cools.today.ui.launcher

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.reactivex.Single
import kotlinx.coroutines.*
import kr.co.cools.today.repo.dao.TodoDao
import kr.co.cools.today.repo.entities.TodoEntity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class LauncherViewModelTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().context

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<LauncherViewModel.LauncherState>

    @Mock
    lateinit var todoDao: TodoDao

    private val launcherInteractor: LauncherInteractor by lazy {
        spy(LauncherInteractor(context = context, todoDao = todoDao))
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    fun tearDown() {
        framework().clearInlineMocks()
    }

    // 1. 할일이 정의되지 않았으면 목록 생성으로 이동한다.
    @Test
    fun forwardTodoListIfEmptyTodo() = runBlocking<Unit> {
        `when`(todoDao.getAll()).thenReturn(Single.just(emptyList()))

        doReturn(Single.just(false)).`when`(launcherInteractor).hasTodoEntity()

        val launcherViewModel = LauncherViewModel(launcherInteractor)
        launcherViewModel.observeForever(observer)
        launcherViewModel.updateLauncherState()

        delay(100)
        verify(observer, timeout(500).times(1)).onChanged(LauncherViewModel.LauncherState.ShowProgress)
        verify(observer, timeout(500).times(1)).onChanged(LauncherViewModel.LauncherState.HideProgress)
        verify(observer, timeout(500).times(1)).onChanged(LauncherViewModel.LauncherState.StartTodoListActivity)
    }

    // 2. 할일이 정의되어 있으면 할일 목록으로 이동한다.
    @Test
    fun forwardJobListIfHasTodo() = runBlocking<Unit> {
        `when`(todoDao.getAll()).thenReturn(Single.just(listOf(TodoEntity())))
        val launcherViewModel = LauncherViewModel(launcherInteractor)
        CoroutineScope(Dispatchers.Main).launch {
            launcherViewModel.observeForever(observer)
            launcherViewModel.updateLauncherState()
        }.join()

        delay(100)
        verify(observer, times(1)).onChanged(LauncherViewModel.LauncherState.ShowProgress)
        verify(observer, times(1)).onChanged(LauncherViewModel.LauncherState.HideProgress)
        verify(observer, times(1)).onChanged(LauncherViewModel.LauncherState.StartJobListActivity)
    }
}

