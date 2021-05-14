package com.io.movies.di.module

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.io.movies.BuildConfig
import com.io.movies.repository.network.MovieService
import com.io.movies.util.Config
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun client(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val interceptor = Interceptor {
            val original = it.request()

            val newUrl = original.url().newBuilder().addQueryParameter("api_key", Config.apiKey).build()

            val request =  original.newBuilder()
                            .url( newUrl)
                            .method(original.method(),original.body())
                            .build()

            return@Interceptor it.proceed(request)
        }

        return OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    fun gson(): Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create()

    @Provides
    @Singleton
    fun retrofit(client: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

    @Provides
    @Singleton
    fun movieService(retrofit: Retrofit): MovieService = retrofit.create(MovieService::class.java)
}