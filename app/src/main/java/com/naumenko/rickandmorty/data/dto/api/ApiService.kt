package com.naumenko.rickandmorty.data.dto.api

import com.naumenko.rickandmorty.data.dto.api.modelsDTO.*
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    @GET("character")
    suspend fun getAllCharacters(): Response<CharactersDTO>

    @GET("character")
    suspend fun getAllCharactersWithPage(@Query("page") page: Int): Response<CharactersDTO>

    @GET("character")
    suspend fun filterCharacters(@QueryMap searchQuery: Map<String, String>): Response<CharactersDTO>

    @GET("character/{characterId}")
    suspend fun loadCharacterById(@Path("characterId") characterId: Int): SingleCharacterDTO

    @GET("character/{charactersId}")
    suspend fun loadListCharactersById(@Path("charactersId") charactersId: List<Int>): List<SingleCharacterDTO>

    @GET("location")
    suspend fun getAllLocations(): Response<LocationsDTO>

    @GET("location")
    suspend fun getAllLocationsWithPage(@Query("page") page: Int): Response<LocationsDTO>

    @GET("location")
    suspend fun filterLocations(@QueryMap searchQuery: Map<String, String>): Response<LocationsDTO>

    @GET("location/{locationId}")
    suspend fun loadLocationById(@Path("locationId") locationId: Int): SingleLocationDTO

    @GET("episode")
    suspend fun getAllEpisodes(): Response<EpisodesDTO>

    @GET("episode/{episodeId}")
    suspend fun loadEpisodeById(@Path("episodeId") episodeId: Int): SingleEpisodeDTO

    @GET("episode/{episodeId}")
    suspend fun loadListEpisodesById(@Path("episodeId") episodesId: List<Int>): List<SingleEpisodeDTO>

    @GET("episode")
    suspend fun getAllEpisodesWithPage(@Query("page") page: Int): Response<EpisodesDTO>

    @GET("episode")
    suspend fun filterEpisodes(@QueryMap searchQuery: Map<String, String>): Response<EpisodesDTO>

}