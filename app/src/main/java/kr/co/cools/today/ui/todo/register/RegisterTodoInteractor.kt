package kr.co.cools.today.ui.todo.register

import io.reactivex.Single
import kr.co.cools.common.extension.asDriver
import kr.co.cools.today.repo.dao.JobDao
import kr.co.cools.today.repo.dao.TodoDao
import kr.co.cools.today.repo.entities.JobEntity
import kr.co.cools.today.repo.entities.TodoEntity
import kr.co.cools.today.ui.utils.DateUtils
import kr.co.cools.today.ui.utils.WeekNumber
import javax.inject.Inject

class RegisterTodoInteractor @Inject constructor(private val todoDao: TodoDao, private val jobDao: JobDao) {
    fun addTodoEntity(title: String, description: String, dayOfWeekNumber: Int): Single<Boolean> {
        return Single.create<Boolean> { emitter ->
            todoDao.insert(
                TodoEntity(
                    title = title,
                    description = description,
                    dayOfWeekNumber = dayOfWeekNumber
                )
            )
            if(dayOfWeekNumber == WeekNumber.getNumber()){
                jobDao.insert(
                    JobEntity(
                        date = DateUtils.getCurrentDate(),
                        todo = TodoEntity(
                            title = title,
                            description = description,
                            dayOfWeekNumber = dayOfWeekNumber
                        )
                    )
                )
            }
            emitter.onSuccess(true)
        }
        .asDriver()
    }
}