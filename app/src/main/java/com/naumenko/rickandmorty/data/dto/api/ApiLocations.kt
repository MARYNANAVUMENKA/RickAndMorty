package com.naumenko.rickandmorty.data.dto.api

import com.naumenko.rickandmorty.data.dto.api.modelsDTO.ResponseLocations
import com.naumenko.rickandmorty.data.dto.api.modelsDTO.SingleLocationDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiLocations {

    @GET("location")
    suspend fun getAllLocations(): ResponseLocations

    @GET("location")
    suspend fun getAllLocationsWithPage(@Query("page") page: Int): ResponseLocations

    @GET("location")
    suspend fun filterLocations(@QueryMap searchQuery: Map<String, String>): ResponseLocations

    @GET("location/{locationId}")
    suspend fun loadLocationById(@Path("locationId") locationId: Int): SingleLocationDTO
}