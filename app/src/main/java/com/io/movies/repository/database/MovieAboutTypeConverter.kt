package com.io.movies.repository.database

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.io.movies.model.Company
import com.io.movies.model.Country
import com.io.movies.model.Genres

class MovieAboutTypeConverter {

    private val gson = GsonBuilder().create()

    @TypeConverter
    fun toStringGender(list: List<Genres>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toListGenres(genresString: String): List<Genres> {
        val type = object : TypeToken<List<Genres>>(){}.type
        return gson.fromJson(genresString, type)
    }

    @TypeConverter
    fun toStringCompany(list: List<Company>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toListCompany(companyString: String): List<Company> {
        val type = object : TypeToken<List<Company>>(){}.type
        return gson.fromJson(companyString, type)
    }


    @TypeConverter
    fun toStringCountry(list: List<Country>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toListCountry(countryString: String): List<Country> {
        val type = object : TypeToken<List<Country>>(){}.type
        return gson.fromJson(countryString, type)
    }
}