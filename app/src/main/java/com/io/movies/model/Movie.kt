package com.io.movies.model

import android.util.Log
import androidx.room.*
import com.google.gson.annotations.SerializedName

data class ResultMovie(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movieInfos: List<MovieInfo>,
)

interface Movie{
    val order: Int
    val id: Int
    val title: String
    val poster: String?
    val voteAverage: Float
    val releaseDate: String?
    var isFavorite:Boolean
}

@Entity(tableName = "movie_info", indices  = [Index(value = ["id"], unique = true)])
data class MovieInfo(
    @PrimaryKey(autoGenerate = true)
    override val order: Int,
    @SerializedName("id")
    @ColumnInfo(name = "id")
    override val id: Int,
    @SerializedName("title")
    override val title: String,
    @SerializedName("poster_path")
    override val poster: String?,
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    override val voteAverage: Float,
    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    override val releaseDate: String?,
    override var isFavorite:Boolean = false
): Movie

@Entity(indices  = [Index(value = ["id"], unique = true)])
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    override val order: Int,
    @SerializedName("id")
    @ColumnInfo(name = "id")
    override val id: Int,
    @SerializedName("title")
    override val title: String,
    @SerializedName("poster_path")
    override val poster: String?,
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    override val voteAverage: Float,
    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    override val releaseDate: String?,
    override var isFavorite:Boolean = true
): Movie

fun Movie.convertToFavorite(): Favorite =
    Favorite(
        0,
        id,
        title,
        poster,
        voteAverage,
        releaseDate
    )

fun Movie.convertToMovieInfo(): MovieInfo =
    MovieInfo(
        order,
        id,
        title,
        poster,
        voteAverage,
        releaseDate,
        isFavorite
    )