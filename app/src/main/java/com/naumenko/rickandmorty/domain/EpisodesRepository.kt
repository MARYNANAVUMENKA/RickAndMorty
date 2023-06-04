package com.naumenko.rickandmorty.domain

import com.naumenko.rickandmorty.domain.models.episodes.SingleEpisode
import kotlinx.coroutines.flow.Flow

interface EpisodesRepository {

    suspend fun loadAllEpisodes(): Flow<List<SingleEpisode>>

    suspend fun loadListEpisodesById(episodesId: List<Int>): Flow<List<SingleEpisode>>

    suspend fun filterEpisodes(searchQuery: HashMap<String, String>): Flow<List<SingleEpisode>>

    suspend fun loadSingleEpisodeById(episodeId: Int): Flow<List<SingleEpisode>>
}