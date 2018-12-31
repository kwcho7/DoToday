package kr.co.cools.today.repo.dao

import android.support.test.runner.AndroidJUnit4
import kr.co.cools.today.repo.entities.DayOfWeek
import kr.co.cools.today.repo.entities.TodoEntity
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TodoDaoTest: BaseDaoTest() {
    lateinit var todoDao:TodoDao

    override fun moduleSetup(component: RepoComponent) {
        todoDao = component.todoDao()
        todoDao.deleteAll()
    }

    @Test
    fun insertOne() {
        val todo = TodoEntity().apply {
            dayOfWeek = DayOfWeek.MON.nameOfDay
            title = "job 1"
            point = 100
        }
        val result = todoDao.insert(todo)
        Assert.assertEquals(1, result.size)
    }

    @Test
    fun insertTwo() {
        val todo = TodoEntity().apply {
            dayOfWeek = DayOfWeek.MON.nameOfDay
            title = "job 1"
            point = 100
        }

        val todo1 = TodoEntity().apply {
            dayOfWeek = DayOfWeek.TUE.nameOfDay
            title = "job 2"
            point = 100
        }

        val result = todoDao.insert(todo, todo1)
        Assert.assertEquals(2, result.size)
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
        todoDao.getAll(DayOfWeek.MON.nameOfDay).subscribe(
            {
                Assert.assertEquals(1, it.size)
            },
            {
                throw it
            }
        )
    }
}
