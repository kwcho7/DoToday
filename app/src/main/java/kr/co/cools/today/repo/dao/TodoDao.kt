package kr.co.cools.today.repo.dao

import android.arch.persistence.room.*
import io.reactivex.Single
import kr.co.cools.today.repo.entities.TodoEntity

@Dao
interface TodoDao {
    @Query("SELECT * FROM todoentity")
    fun getAll(): Single<List<TodoEntity>>

    @Query("SELECT * FROM todoentity WHERE dayOfWeek IN (:dayOfWeek)")
    fun getAll(dayOfWeek: String): Single<List<TodoEntity>>

    @Insert
    fun insert(vararg todo: TodoEntity): LongArray

    @Update
    fun update(todo: TodoEntity)

    @Delete
    fun delete(todo: TodoEntity)

    @Query("DELETE FROM TodoEntity")
    fun deleteAll()
}