package com.naumenko.rickandmorty.data.dto.api

import com.naumenko.rickandmorty.data.dto.api.modelsDTO.ResponseCharacters
import com.naumenko.rickandmorty.data.dto.api.modelsDTO.SingleCharacterDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiCharacters {

    @GET("character")
    suspend fun getAllCharacters(): ResponseCharacters

    @GET("character")
    suspend fun getAllCharactersWithPage(@Query("page") page: Int): ResponseCharacters

    @GET("character")
    suspend fun filterCharacters(@QueryMap searchQuery: Map<String, String>): ResponseCharacters

    @GET("character/{characterId}")
    suspend fun loadCharacterById(@Path("characterId") characterId: Int): SingleCharacterDTO

    @GET("character/{charactersId}")
    suspend fun loadListCharactersById(@Path("charactersId") charactersId: List<Int>): List<SingleCharacterDTO>
}