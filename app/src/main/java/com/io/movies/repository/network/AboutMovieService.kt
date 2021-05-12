package com.io.movies.repository.network

import com.io.movies.model.AboutMovie
import com.io.movies.model.ResultCredit
import com.io.movies.util.Config
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AboutMovieService {

    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: Int, @Query("api_key") key: String = Config.apiKey): Single<AboutMovie>

    @GET("movie/{movie_id}/credits")
    fun getCredits(@Path("movie_id") movieId: Int, @Query("api_key") key: String = Config.apiKey): Single<ResultCredit>
}