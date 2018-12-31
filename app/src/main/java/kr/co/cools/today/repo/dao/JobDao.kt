package kr.co.cools.today.repo.dao

import android.arch.persistence.room.*
import io.reactivex.Single
import kr.co.cools.today.repo.entities.JobEntity

@Dao
interface JobDao{
    @Query("SELECT * FROM jobentity")
    fun getAll() : Single<List<JobEntity>>

    @Query("SELECT * FROM jobentity WHERE dayOfWeek IN (:dayOfWeek)")
    fun getAll(dayOfWeek: String) : Single<List<JobEntity>>

    @Insert
    fun insert(vararg jobTodo: JobEntity): LongArray

    @Delete
    fun delete(jobTodo: JobEntity)

    @Query("DELETE FROM JobEntity")
    fun deleteAll()

    @Update
    fun update(jobTodo: JobEntity)
}