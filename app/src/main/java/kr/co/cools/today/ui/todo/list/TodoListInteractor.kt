package kr.co.cools.today.ui.todo.list

import androidx.lifecycle.LiveData
import kr.co.cools.today.repo.dao.TodoDao
import kr.co.cools.today.repo.entities.TodoEntity
import javax.inject.Inject

class TodoListInteractor @Inject constructor(val todoDao: TodoDao) {
    fun getTodoLiveData(): LiveData<List<TodoEntity>> {
        return todoDao.getAllLiveData()
    }
}