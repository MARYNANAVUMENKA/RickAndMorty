package com.naumenko.rickandmorty.viewmodel.test

import com.naumenko.rickandmorty.domain.models.characters.LocationCharacter
import com.naumenko.rickandmorty.domain.models.characters.OriginCharacter
import com.naumenko.rickandmorty.domain.models.characters.SingleCharacter
import com.naumenko.rickandmorty.domain.models.episodes.SingleEpisode

fun createSingleCharacters(
    id: Int,
    created: String,
    episode: List<String>,
    gender: String,
    image: String,
    locationCharacter: LocationCharacter,
    name: String,
    originCharacter: OriginCharacter,
    species: String,
    status: String,
    type: String,
    url: String
) = SingleCharacter(
    id = id,
    created = created,
    episode = episode,
    gender = gender,
    image = image,
    locationCharacter = locationCharacter,
    name = name,
    originCharacter = originCharacter,
    species = species,
    status = status,
    type = type,
    url = url
)

fun createSingleEpisodes(
    airDate: String,
    characters: List<String>,
    created: String,
    episode: String,
    id: Int,
    name: String,
    url: String
) = SingleEpisode(
    airDate = airDate,
    characters = characters,
    created = created,
    episode = episode,
    id = id,
    name = name,
    url = url
)