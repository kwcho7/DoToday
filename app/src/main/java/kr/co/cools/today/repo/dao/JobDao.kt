package kr.co.cools.today.repo.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Single
import kr.co.cools.today.repo.entities.JobEntity

@Dao
interface JobDao{
    @Query("SELECT * FROM jobentity")
    fun getAll() : Single<List<JobEntity>>

    @Query("SELECT * FROM jobentity")
    fun getAllLiveData() : LiveData<List<JobEntity>>

    @Query("SELECT * FROM jobentity WHERE date IN (:date)")
    fun getAllLiveData(date: String) : LiveData<List<JobEntity>>

    @Query("SELECT * FROM jobentity WHERE date IN (:date) AND completed IN (:completed)")
    fun getAllLiveData(date: String, completed: Boolean) : LiveData<List<JobEntity>>


    @Query("SELECT * FROM jobentity WHERE dayOfWeekNumber IN (:dayOfWeek)")
    fun getAll(dayOfWeek: Int) : Single<List<JobEntity>>

    @Query("SELECT * FROM jobentity WHERE date IN (:date)")
    fun getAll(date: String) : Single<List<JobEntity>>

    @Query("SELECT * FROM jobentity WHERE date IN (:date) AND completed IN (:completed)")
    fun getAll(date: String, completed: Boolean) : Single<List<JobEntity>>

    @Insert
    fun insert(vararg jobTodo: JobEntity): LongArray

    @Delete
    fun delete(jobTodo: JobEntity)

    @Query("DELETE FROM JobEntity")
    fun deleteAll()

    @Update
    fun update(jobTodo: JobEntity)
}