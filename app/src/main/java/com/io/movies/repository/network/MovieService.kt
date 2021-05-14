package com.io.movies.repository.network

import com.io.movies.model.AboutMovie
import com.io.movies.model.ResultCredit
import com.io.movies.model.ResultMovie
import com.io.movies.util.Config
import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query



interface MovieService {

    @GET("movie/popular")
    fun getMovies(@Query("page") page: Int): Single<ResultMovie>

    @GET("search/movie")
    fun getSearchMovies(@Query("page") page: Int, @Query("query") query: String): Single<ResultMovie>

    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: Int): Single<AboutMovie>

    @GET("movie/{movie_id}/credits")
    fun getCredits(@Path("movie_id") movieId: Int): Single<ResultCredit>
}