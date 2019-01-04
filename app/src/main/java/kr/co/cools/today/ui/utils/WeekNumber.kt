package kr.co.cools.today.ui.utils

import android.content.Context
import kr.co.cools.today.R
import kr.co.cools.today.repo.entities.DayOfWeek
import java.util.*

class WeekNumber {

    companion object {

        fun getNumber(): Int {
            val calendar = Calendar.getInstance()
            when(calendar.get(Calendar.DAY_OF_WEEK)){
                Calendar.MONDAY -> {
                    return DayOfWeek.MON.valueOfDay
                }
                Calendar.TUESDAY -> {
                    return DayOfWeek.TUE.valueOfDay
                }
                Calendar.WEDNESDAY -> {
                    return DayOfWeek.WED.valueOfDay
                }
                Calendar.THURSDAY -> {
                    return DayOfWeek.THU.valueOfDay
                }
                Calendar.FRIDAY -> {
                    return DayOfWeek.FRI.valueOfDay
                }
                Calendar.SATURDAY -> {
                    return DayOfWeek.SAT.valueOfDay
                }
                Calendar.SUNDAY -> {
                    return DayOfWeek.SUN.valueOfDay
                }
            }
            return 0
        }

        fun getWeekString(context: Context, weekNumber: Int): String {
            when(weekNumber){
                DayOfWeek.MON.valueOfDay -> {
                    return context.getString(R.string.monday)
                }
                DayOfWeek.TUE.valueOfDay -> {
                    return context.getString(R.string.tuesday)
                }
                DayOfWeek.WED.valueOfDay -> {
                    return context.getString(R.string.wednesday)
                }
                DayOfWeek.THU.valueOfDay -> {
                    return context.getString(R.string.thusday)
                }
                DayOfWeek.FRI.valueOfDay -> {
                    return context.getString(R.string.friday)
                }
                DayOfWeek.SAT.valueOfDay -> {
                    return context.getString(R.string.saturday)
                }
                DayOfWeek.SUN.valueOfDay -> {
                    return context.getString(R.string.sunday)
                }
                else  -> {
                    return ""
                }
            }
        }
    }

}