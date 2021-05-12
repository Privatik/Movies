package com.io.movies.repository.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateTypeConverter {

    @TypeConverter
    fun toString(date: Date?): String? {
        if (date == null) return null
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date)
    }

    @TypeConverter
    fun toDate(data: String?): Date? {
        if (data == null) return null
       return SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(data)
    }
}