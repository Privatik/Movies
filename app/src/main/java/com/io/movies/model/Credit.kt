package com.io.movies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.io.movies.repository.database.CreditTypeConverter
import com.io.movies.repository.database.DateTypeConverter
import com.io.movies.repository.database.MovieAboutTypeConverter

@Entity(tableName = "result_credit")
@TypeConverters(CreditTypeConverter::class)
data class ResultCredit(
    @SerializedName("id")
    @PrimaryKey
    val id: Int,
    @SerializedName("cast")
    val cast: List<Credit>
)

data class Credit (
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_path")
    val profile:String,
    @SerializedName("character")
    val character: String
)