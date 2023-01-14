package com.naumenko.rickandmorty.data.dto.api.modelsDTO

import com.google.gson.annotations.SerializedName

data class EpisodesDTO(
    @SerializedName("info")
    val infoEpisodeDTO: InfoEpisodeDTO?,
    @SerializedName("results")
    val singleEpisodeDTO: List<SingleEpisodeDTO>
)

data class InfoEpisodeDTO(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("prev")
    val prev: Any?
)

data class SingleEpisodeDTO(
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("characters")
    val characters: List<String>,
    @SerializedName("created")
    val created: String,
    @SerializedName("episode")
    val episode: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)