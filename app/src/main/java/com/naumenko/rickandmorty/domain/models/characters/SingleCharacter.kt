package com.naumenko.rickandmorty.domain.models.characters

data class SingleCharacter(
    val id: Int,
    val created: String,
    val episode: List<String>,
    val gender: String,
    val image: String,
    val locationCharacter: LocationCharacter,
    val name: String,
    val originCharacter: OriginCharacter,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)

data class LocationCharacter(
    val name: String,
    val url: String
)

data class OriginCharacter(
    val name: String,
    val url: String
)