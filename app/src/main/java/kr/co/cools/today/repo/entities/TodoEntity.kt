package kr.co.cools.today.repo.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) var todoIndex: Int = 0,
    /**
     * [DayOfWeek] - 월,화,수,목,금,토,일
     */
    @ColumnInfo(name = "dayOfWeek") var dayOfWeek: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "point") var point: Int = 0
)

enum class DayOfWeek(val nameOfDay: String) {
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일")
}