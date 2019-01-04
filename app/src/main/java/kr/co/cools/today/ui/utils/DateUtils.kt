package kr.co.cools.today.ui.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils{
    companion object {

        fun getCurrentDate(): String{
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            return simpleDateFormat.format(Date())
        }
    }
}