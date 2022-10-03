package com.medicine.remedy.utils

import android.annotation.SuppressLint
import com.medicine.remedy.utils.AppConstants.Companion.MMM_DD_YYYY
import com.medicine.remedy.utils.AppConstants.Companion.YYYY_MM_DD_HH_MM_SS
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Date 12/22/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class DateTimeUtils
{
    companion object{

        /**
         * get current date and time from system
         * @return system date and time
         */
        @SuppressLint("SimpleDateFormat")
        fun SYSTEM_CURRENT_DATE_TIME(): String? {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return dateFormat.format(calendar.time)
        }

        @SuppressLint("SimpleDateFormat")
        fun dateFormat(value: String, input: String, output: String): String {
            val inputFormat = SimpleDateFormat(input)
            val dateFormat = SimpleDateFormat(output)
            val date: Date
            var result = ""
            try {
                date = inputFormat.parse(value)
                result = dateFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }


        /**
         * ...calculate how much time ago from current time
         * ...if time is less then 1 minute then return seconds
         * ...if time is less then 1 hour then return minute
         * ...if time is less then 1 day then return hour
         * ...if time is more than 2  days then return time and date
         * @param dateTime created date
         * @return calculated time
         */
        fun timeAgo(dateTime: String): String {
            return try {
                @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat(YYYY_MM_DD_HH_MM_SS)
                val past = format.parse(dateTime)
                val now = Date()
                val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past!!.time)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
                val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
                val days = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
                when {
                    seconds in 1..59 -> "$seconds seconds ago"
                    minutes in 1..59 -> "$minutes minutes ago"
                    hours in 1..23 -> "$hours hours ago"
                    days in 1..6 -> "$days days ago"
                    else -> dateFormat(dateTime, YYYY_MM_DD_HH_MM_SS, MMM_DD_YYYY)
                }
            } catch (j: java.lang.Exception) {
                dateFormat(dateTime, YYYY_MM_DD_HH_MM_SS, MMM_DD_YYYY)
            }
        }
    }
}