package com.naumenko.rickandmorty.data.dto.api.modelsDTO

import com.google.gson.annotations.SerializedName

data class CharactersDTO(
    @SerializedName("info")
    val info: InfoCharacterDto,
    @SerializedName("results")
    val singleCharacterDTO: List<SingleCharacterDTO>
)

data class InfoCharacterDto(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("prev")
    val prev: Any
)

data class SingleCharacterDTO(
    @SerializedName("created")
    val created: String,
    @SerializedName("episode")
    val episode: List<String>,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("location")
    val location: LocationCharacterDTO,
    @SerializedName("name")
    val name: String,
    @SerializedName("origin")
    val origin: OriginCharacterDTO,
    @SerializedName("species")
    val species: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String
)

data class LocationCharacterDTO(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class OriginCharacterDTO(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)