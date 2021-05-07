package com.io.movies.repository.network

import com.io.movies.model.ResultMovie
import com.io.movies.util.Config
import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.Query



interface MovieService {

    @GET("movie/popular")
    fun getMovies(@Query("page") page: Int, @Query("api_key") key: String = Config.apiKey): Single<ResultMovie>

    @GET("search/movie")
    fun getSearchMovies(@Query("page") page: Int, @Query("query") query: String, @Query("api_key") key: String = Config.apiKey): Single<ResultMovie>
}