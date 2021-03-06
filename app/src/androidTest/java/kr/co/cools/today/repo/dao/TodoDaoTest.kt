package kr.co.cools.today.repo.dao

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kr.co.cools.common.logger.Logger
import kr.co.cools.today.repo.entities.DayOfWeek
import kr.co.cools.today.repo.entities.TodoEntity
import kr.co.cools.today.todayContext
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class TodoDaoTest {
    lateinit var todoDao:TodoDao

    @Before
    fun setup() {
        val todayContext = InstrumentationRegistry.getInstrumentation().targetContext.todayContext()
        todoDao = todayContext.todoDao()
        todoDao.deleteAll()
    }

    @Test
    fun insertOne() {
        val todo = TodoEntity().apply {
            dayOfWeekNumber = 0
            title = "job 1"
            description = "job 1 description"
            point = 100
        }
        val result = todoDao.insert(todo)
        Assert.assertEquals(1, result.size)
    }

    @Test
    fun insertTwo() {
        val todo = TodoEntity().apply {
            dayOfWeekNumber = 1
            title = "job 1"
            point = 100
        }

        val todo1 = TodoEntity().apply {
            dayOfWeekNumber = 2
            title = "job 2"
            point = 100
        }

        val result = todoDao.insert(todo, todo1)
        Assert.assertEquals(2, result.size)
    }

    @Test
    fun insertMany() {
        insertTodo(DayOfWeek.MON, 10)
        insertTodo(DayOfWeek.TUE, 10)
        insertTodo(DayOfWeek.WED, 10)
        insertTodo(DayOfWeek.THU, 10)
        insertTodo(DayOfWeek.SAT, 10)
        insertTodo(DayOfWeek.SUN, 10)
    }

    private fun insertTodo(_dayOfWeek: DayOfWeek, count: Int){
        for(i in 0.. count){
            val todo = TodoEntity().apply {
                dayOfWeekNumber = _dayOfWeek.valueOfDay
                title = "${_dayOfWeek.valueOfDay} job $i"
                description = "${_dayOfWeek.valueOfDay} job $i description"
                point = 100
            }
            val result = todoDao.insert(todo)
        }
    }

    @Test
    fun getAll() {
        insertOne()
        insertTwo()
        todoDao.getAll().subscribe(
            {
                Assert.assertEquals(3, it.size)
            },
            {
                throw it
            }
        )
    }

    @Test
    fun deleteTodos() {
        insertTwo()
        todoDao.getAll()
            .flatMap {
                it.forEach {
                    todoDao.delete(it)
                }
                todoDao.getAll()
            }.subscribe(
                {
                    Assert.assertEquals(0, it.size)
                },
                {
                    throw it
                }
            )
    }

    @Test
    fun getMonTodos() {
        insertTwo()

        todoDao.getAll(DayOfWeek.TUE.valueOfDay).subscribe(
            {
                Logger.v("it.${it.size}")
                Assert.assertEquals(1, it.size)
            },
            {
                throw it
            }
        )
    }
}
