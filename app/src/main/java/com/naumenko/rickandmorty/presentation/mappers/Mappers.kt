package com.naumenko.rickandmorty.presentation.mappers

import com.naumenko.rickandmorty.domain.models.characters.SingleCharacter
import com.naumenko.rickandmorty.domain.models.episodes.SingleEpisode
import com.naumenko.rickandmorty.domain.models.locations.SingleLocation
import com.naumenko.rickandmorty.presentation.characters.models.SingleCharacterListItem
import com.naumenko.rickandmorty.presentation.episodes.models.SingleEpisodeListItem
import com.naumenko.rickandmorty.presentation.locations.models.SingleLocationListItem

fun SingleCharacter.toSingleCharacterListItem(): SingleCharacterListItem {
    return SingleCharacterListItem(originSingleCharacter = this)
}

fun SingleEpisode.toSingleEpisodeListItem(): SingleEpisodeListItem {
    return SingleEpisodeListItem(originSingleEpisode = this)
}

fun SingleLocation.toSingleLocationListItem(): SingleLocationListItem {
    return SingleLocationListItem(originSingleLocation = this)
}