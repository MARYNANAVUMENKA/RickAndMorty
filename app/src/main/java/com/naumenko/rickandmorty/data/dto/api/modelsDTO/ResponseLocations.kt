package com.naumenko.rickandmorty.data.dto.api.modelsDTO

import com.google.gson.annotations.SerializedName

data class ResponseLocations(
    @SerializedName("info")
    val infoLocationDTO: InfoLocationDTO,
    @SerializedName("results")
    val singleLocationDTO: List<SingleLocationDTO>
)

data class InfoLocationDTO(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("prev")
    val prev: Any
)

data class SingleLocationDTO(
    @SerializedName("created")
    val created: String,
    @SerializedName("dimension")
    val dimension: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("residents")
    val residents: List<String>,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String
)