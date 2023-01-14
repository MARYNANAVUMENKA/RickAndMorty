package com.naumenko.rickandmorty.presentation.episodes.models

import com.naumenko.rickandmorty.domain.models.episodes.SingleEpisode

data class SingleEpisodeListItem(
    val originSingleEpisode: SingleEpisode
) {
    val airDate: String get() = originSingleEpisode.airDate
    val characters: List<String> get() = originSingleEpisode.characters
    val created: String get() = originSingleEpisode.created
    val episode: String get() = originSingleEpisode.episode
    val id: Int get() = originSingleEpisode.id
    val name: String get() = originSingleEpisode.name
    val url: String get() = originSingleEpisode.url
}