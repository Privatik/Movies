package com.io.movies.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.io.movies.repository.database.DateTypeConverter
import com.io.movies.repository.database.MovieAboutTypeConverter
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "info_about_movie")
@TypeConverters(DateTypeConverter::class, MovieAboutTypeConverter::class)
data class AboutMovie(
    @SerializedName("id")
    @PrimaryKey
    val id:Int,
    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    val backdrop: String?,
    @SerializedName("adult")
    @ColumnInfo(name = "adult")
    val adult: Boolean,
    @SerializedName("budget")
    @ColumnInfo(name = "budget")
    val budget: Int,
    @SerializedName("homepage")
    @ColumnInfo(name = "homepage")
    val homepage: String?,
    @SerializedName("imdb_id")
    @ColumnInfo(name = "imdb_id")
    val imdb: String?,
    @SerializedName("genres")
    @ColumnInfo(name = "genres")
    val genres: List<Genres>,
    @SerializedName("overview")
    @ColumnInfo(name = "overview")
    val overview: String?,
    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String,
    @SerializedName("status")
    @ColumnInfo(name = "status")
    val status: String,
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    val voteAverage: Float,
    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    val releaseDate: Date?,
    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    val poster: String?,
    @SerializedName("production_companies")
    @ColumnInfo(name = "production_companies")
    val companies: List<Company>,
    @SerializedName("production_countries")
    @ColumnInfo(name = "production_countries")
    val countries: List<Country>
)

data class Genres(
    @SerializedName("name")
    val name: String,
)


data class Company(
    @SerializedName("logo_path")
    val logo: String?,
    @SerializedName("name")
    val name: String
)


data class Country(
    @SerializedName("iso_3166_1")
    val country: String
)

fun AboutMovie.convertToFavorite(): Favorite{
    return Favorite(
        order = 0,
        id = this.id,
        title = this.title,
        poster = this.poster,
        voteAverage = this.voteAverage,
        releaseDate = releaseDate?.let {SimpleDateFormat("yyyy-MM-dd", Locale.US).format(it)}
    )
}
