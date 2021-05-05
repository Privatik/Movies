package com.io.movies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ResultMovie(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movies: List<Movie>,
)

@Entity
data class Movie(
    @SerializedName("id")
    @PrimaryKey
    val id: Int,
    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    val backImage: String,
    @SerializedName("original_title")
    val title: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val poster: String,
    @SerializedName("popularity")
    val popularity: Float,
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    val voteAverage: Float,
    var like:Boolean = false
)