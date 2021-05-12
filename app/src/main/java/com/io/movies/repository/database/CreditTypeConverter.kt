package com.io.movies.repository.database

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.io.movies.model.Credit

class CreditTypeConverter {

    private val gson = GsonBuilder().create()

    @TypeConverter
    fun toStringGender(list: List<Credit>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toListGenres(genresString: String): List<Credit> {
        val type = object : TypeToken<List<Credit>>(){}.type
        return gson.fromJson(genresString, type)
    }
}