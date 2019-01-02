package kr.co.cools.today.repo.dao

import android.support.test.runner.AndroidJUnit4
import kr.co.cools.today.repo.entities.DayOfWeek
import kr.co.cools.today.repo.entities.JobEntity
import kr.co.cools.today.repo.entities.TodoEntity
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

@RunWith(AndroidJUnit4::class)
class JobDaoTest: BaseDaoTest() {

    lateinit var jobDao: JobDao
    lateinit var todoDao: TodoDao

    override fun moduleSetup(component: RepoComponent) {
        jobDao = component.jobDao()
        todoDao = component.todoDao()
        jobDao.deleteAll()
        todoDao.deleteAll()

        val result = todoDao.insert(
            TodoEntity().apply {
                title = "job todo mon 1"
                dayOfWeekNumber = DayOfWeek.MON.valueOfDay
                point = 100
            },
            TodoEntity().apply {
                title = "job todo mon 2"
                dayOfWeekNumber = DayOfWeek.MON.valueOfDay
                point = 100
            },
            TodoEntity().apply {
                title = "job todo 2"
                dayOfWeekNumber = DayOfWeek.TUE.valueOfDay
                point = 100
            },
            TodoEntity().apply {
                title = "job todo 3"
                dayOfWeekNumber = DayOfWeek.WED.valueOfDay
                point = 100
            },
            TodoEntity().apply {
                title = "job todo 4"
                dayOfWeekNumber = DayOfWeek.THU.valueOfDay
                point = 100
            }
        )
        Timber.v("setup db.${result.size}")
    }

    @Test
    fun insertMonJob() {
        todoDao.getAll(DayOfWeek.MON.valueOfDay)
            .map {
                it.forEach {
                    jobDao.insert(
                        JobEntity().apply {
                            date = "121212"
                            todo = it
                        }
                    )
                }
            }
            .flatMap {
                jobDao.getAll()
            }
            .subscribe(
                {
                    Timber.v("insertMonJob.${it}")
                },
                {
                    throw  it
                }
            )
    }

    @Test
    fun insertAllJob() {
        todoDao.getAll()
            .map {
                it.forEach {
                    jobDao.insert(
                        JobEntity().apply {
                            date = "121212"
                            todo = it
                        }
                    )
                }
            }
            .flatMap {
                jobDao.getAll()
            }
            .subscribe(
                {
                    Timber.v("insertMonJob.${it}")
                },
                {
                    throw  it
                }
            )
    }

    @Test
    fun getAll() {
        insertAllJob()
        jobDao.getAll()
            .subscribe(
            {
                Timber.v("JobDao getAll.${it}")
            },
            {
                throw it
            }
        )
    }

    @Test
    fun getMonAll() {
        insertAllJob()
        jobDao.getAll(DayOfWeek.THU.valueOfDay)
            .subscribe(
                {
                    Timber.v("JobDao getMonAll.${it}")
                },
                {
                    throw it
                }
            )
    }

    @Test
    fun deleteMon() {
        insertAllJob()
        jobDao.getAll()
            .flatMap {
                it.forEach {
                    if(it.todo?.dayOfWeekNumber == DayOfWeek.MON.valueOfDay){
                        jobDao.delete(it)
                    }
                }
                jobDao.getAll()
            }
            .subscribe(
                {
                    Timber.v("JobDao getMonAll.${it}")
                },
                {
                    throw it
                }
            )
    }

    @Test
    fun upateMonCompleted() {
        insertAllJob()
        jobDao.getAll(DayOfWeek.MON.valueOfDay)
            .flatMap{
                it.forEach {
                    it.completed = true
                    jobDao.update(it)
                }
                jobDao.getAll()
            }
            .subscribe(
                {
                    Timber.v("list.$it")
                },
                {
                    throw it
                }
            )
    }
}