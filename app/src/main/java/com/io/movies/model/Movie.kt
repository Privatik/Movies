package com.io.movies.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.io.movies.repository.database.DateTypeConverter
import java.util.*

data class ResultMovie(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movies: List<Movie>,
)

@Entity
@TypeConverters(DateTypeConverter::class)
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val key: Int,
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int,
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("original_title")
    val title: String,
    @SerializedName("poster_path")
    val poster: String?,
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    val voteAverage: Float,
    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    val releaseDate: Date?,
    var like:Boolean = false
)