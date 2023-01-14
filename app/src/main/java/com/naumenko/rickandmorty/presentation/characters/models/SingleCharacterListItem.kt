package com.naumenko.rickandmorty.presentation.characters.models

import com.naumenko.rickandmorty.domain.models.characters.SingleCharacter

data class SingleCharacterListItem(
    val originSingleCharacter: SingleCharacter,
) {
    val episode: List<String> get() = originSingleCharacter.episode
    val id: Int get() = originSingleCharacter.id
    val name: String get() = originSingleCharacter.name
    val species: String get() = originSingleCharacter.species
    val status: String get() = originSingleCharacter.status
    val gender: String get() = originSingleCharacter.gender
    val image: String get() = originSingleCharacter.image
    val locationName: String get() = originSingleCharacter.locationCharacter.name
    val originName: String get() = originSingleCharacter.originCharacter.name
    val locationUrl: String get() = originSingleCharacter.locationCharacter.url
    val originUrl: String get() = originSingleCharacter.originCharacter.url
}