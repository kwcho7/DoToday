package kr.co.cools.today.ui.launcher

import android.content.Context
import io.reactivex.Single
import kr.co.cools.today.repo.dao.TodoDao
import javax.inject.Inject

open class LauncherInteractor @Inject constructor(val context: Context, var todoDao: TodoDao) {

    open fun hasTodoEntity(): Single<Boolean> {
        return todoDao.getAll()
            .map { it.isNotEmpty() }
    }
}

