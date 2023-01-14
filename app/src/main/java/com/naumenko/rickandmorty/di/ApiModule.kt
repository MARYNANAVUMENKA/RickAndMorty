package com.naumenko.rickandmorty.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.naumenko.rickandmorty.data.dto.api.ApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    private val BASE_URL = "https://rickandmortyapi.com/api/"

    @Provides
    @Singleton
    fun apiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()
}
