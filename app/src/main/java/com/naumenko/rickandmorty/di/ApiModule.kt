package com.naumenko.rickandmorty.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.naumenko.rickandmorty.data.dto.api.ApiCharacters
import com.naumenko.rickandmorty.data.dto.api.ApiEpisodes
import com.naumenko.rickandmorty.data.dto.api.ApiLocations
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
    fun provideApiCharacters(retrofit: Retrofit): ApiCharacters = retrofit.create(ApiCharacters::class.java)

    @Provides
    @Singleton
    fun provideApiEpisodes(retrofit: Retrofit): ApiEpisodes = retrofit.create(ApiEpisodes::class.java)

    @Provides
    @Singleton
    fun provideApiLocations(retrofit: Retrofit): ApiLocations = retrofit.create(ApiLocations::class.java)

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
