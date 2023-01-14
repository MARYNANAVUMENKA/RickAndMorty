package com.naumenko.rickandmorty.data.dto

import com.naumenko.rickandmorty.data.dto.api.modelsDTO.*
import com.naumenko.rickandmorty.data.dto.db.characters.LocationCharacterEntity
import com.naumenko.rickandmorty.data.dto.db.characters.OriginCharacterEntity
import com.naumenko.rickandmorty.data.dto.db.characters.SingleCharacterEntity
import com.naumenko.rickandmorty.data.dto.db.episodes.SingleEpisodeEntity
import com.naumenko.rickandmorty.data.dto.db.locations.SingleLocationEntity
import com.naumenko.rickandmorty.domain.models.characters.LocationCharacter
import com.naumenko.rickandmorty.domain.models.characters.OriginCharacter
import com.naumenko.rickandmorty.domain.models.characters.SingleCharacter
import com.naumenko.rickandmorty.domain.models.episodes.SingleEpisode
import com.naumenko.rickandmorty.domain.models.locations.SingleLocation

fun SingleCharacterDTO.toSingleCharacter(): SingleCharacter {
    return SingleCharacter(
        id,
        created,
        episode,
        gender,
        image,
        location.toLocation(),
        name,
        origin.toOrigin(),
        species,
        status,
        type,
        url
    )
}

fun SingleCharacterEntity.toSingleCharacter(): SingleCharacter {
    return SingleCharacter(
        id,
        created,
        episode,
        gender,
        image,
        location.toLocation(),
        name,
        origin.toOrigin(),
        species,
        status,
        type,
        url
    )
}

fun SingleCharacter.toSingleCharacterEntity(): SingleCharacterEntity {
    return SingleCharacterEntity(
        id, created, episode, gender, image, locationCharacter.toLocation(),
        name,
        originCharacter.toOrigin(),
        species,
        status,
        type,
        url
    )
}

fun LocationCharacter.toLocation(): LocationCharacterEntity {
    return LocationCharacterEntity(name, url)
}

fun OriginCharacter.toOrigin(): OriginCharacterEntity {
    return OriginCharacterEntity(name, url)
}

fun LocationCharacterDTO.toLocation(): LocationCharacter {
    return LocationCharacter(name, url)
}

fun OriginCharacterDTO.toOrigin(): OriginCharacter {
    return OriginCharacter(name, url)
}

fun LocationCharacterEntity.toLocation(): LocationCharacter {
    return LocationCharacter(name, url)
}

fun OriginCharacterEntity.toOrigin(): OriginCharacter {
    return OriginCharacter(name, url)
}

fun SingleLocationDTO.toSingleLocation(): SingleLocation {
    return SingleLocation(created, dimension, id, name, residents, type, url)
}

fun SingleLocationEntity.toSingleLocation(): SingleLocation {
    return SingleLocation(created, dimension, id, name, residents, type, url)
}

fun SingleLocation.toSingleLocationEntity(): SingleLocationEntity {
    return SingleLocationEntity(created, dimension, id, name, residents, type, url)
}

fun SingleEpisodeDTO.toSingleEpisode(): SingleEpisode {
    return SingleEpisode(airDate, characters, created, episode, id, name, url)
}

fun SingleEpisodeEntity.toSingleEpisode(): SingleEpisode {
    return SingleEpisode(airDate, characters, created, episode, id, name, url)
}

fun SingleEpisode.toSingleEpisodeEntity(): SingleEpisodeEntity {
    return SingleEpisodeEntity(airDate, characters, created, episode, id, name, url)
}