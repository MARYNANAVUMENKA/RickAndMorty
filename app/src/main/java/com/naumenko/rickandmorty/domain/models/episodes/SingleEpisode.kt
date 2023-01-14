package com.naumenko.rickandmorty.domain.models.episodes

data class SingleEpisode(
    val airDate: String,
    val characters: List<String>,
    val created: String,
    val episode: String,
    val id: Int,
    val name: String,
    val url: String
)