package kr.co.cools.today.repo.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) var todoIndex: Int = 0,
    /**
     * [DayOfWeek] - 0-월, 1-화, 2-수, 3-목, 4-금, 5-토, 6-일
     */
    @ColumnInfo(name = "dayOfWeekNumber") var dayOfWeekNumber: Int = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "point") var point: Int = 0
)

enum class DayOfWeek(val valueOfDay: Int) {
    MON(0),
    TUE(1),
    WED(2),
    THU(3),
    FRI(4),
    SAT(5),
    SUN(6)
}