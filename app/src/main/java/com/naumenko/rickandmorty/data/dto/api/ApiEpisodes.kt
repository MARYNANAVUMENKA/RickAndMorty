package com.naumenko.rickandmorty.data.dto.api

import com.naumenko.rickandmorty.data.dto.api.modelsDTO.ResponseEpisodes
import com.naumenko.rickandmorty.data.dto.api.modelsDTO.SingleEpisodeDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiEpisodes {

    @GET("episode")
    suspend fun getAllEpisodes(): ResponseEpisodes

    @GET("episode/{episodeId}")
    suspend fun loadEpisodeById(@Path("episodeId") episodeId: Int): SingleEpisodeDTO

    @GET("episode/{episodeId}")
    suspend fun loadListEpisodesById(@Path("episodeId") episodesId: List<Int>): List<SingleEpisodeDTO>

    @GET("episode")
    suspend fun getAllEpisodesWithPage(@Query("page") page: Int): ResponseEpisodes

    @GET("episode")
    suspend fun filterEpisodes(@QueryMap searchQuery: Map<String, String>): ResponseEpisodes
}