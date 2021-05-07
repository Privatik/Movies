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
    var id: Int,
    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    var backImage: String?,
    @SerializedName("original_title")
    var title: String,
    @SerializedName("overview")
    var overview: String,
    @SerializedName("poster_path")
    var poster: String?,
    @SerializedName("popularity")
    var popularity: Float,
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    var voteAverage: Float,
    var like:Boolean = false
)

fun Movie.replace(movie: Movie){
    val z = movie.copy(
            id = movie.id,
            backImage = movie.backImage,
            title = movie.title,
            poster = movie.poster,
            overview = movie.overview,
            popularity = movie.popularity,
            voteAverage = movie.voteAverage,
            like = movie.like
    )



    movie.id = id
    movie.backImage = backImage
    movie.title = title
    movie.poster = poster
    movie.overview = overview
    movie.popularity = popularity
    movie.voteAverage = voteAverage
    movie.like = like

    id = z.id
    backImage = z.backImage
    title = z.title
    poster = z.poster
    overview = z.overview
    popularity = z.popularity
    voteAverage = z.voteAverage
    like = z.like
}