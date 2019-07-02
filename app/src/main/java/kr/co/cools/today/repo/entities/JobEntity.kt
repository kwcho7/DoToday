package kr.co.cools.today.repo.entities

import androidx.room.*


@Entity
data class JobEntity(
    @PrimaryKey(autoGenerate = true)
    var jobIndex: Int = 0,

    @ColumnInfo(name = "completed")
    var completed: Boolean = false,

    @ColumnInfo(name = "date")
    var date: String = "",

    @Embedded
    var todo: TodoEntity? = null
)