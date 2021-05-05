package com.io.movies.repository.network

import com.io.movies.model.ResultMovie
import com.io.movies.util.Config
import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.Query


interface MovieService {

    @GET("movie/popular")
    fun getMovie(@Query("page") page: Int, @Query("api_key") key: String = Config.apiKey): Single<ResultMovie>
}