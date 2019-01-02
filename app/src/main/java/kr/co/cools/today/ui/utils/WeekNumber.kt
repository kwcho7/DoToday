package kr.co.cools.today.ui.utils

import android.content.Context
import kr.co.cools.today.R
import kr.co.cools.today.repo.entities.DayOfWeek

class WeekNumber {

    companion object {
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