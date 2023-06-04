package com.naumenko.rickandmorty.viewmodel.test

import com.naumenko.rickandmorty.domain.EpisodesRepository
import com.naumenko.rickandmorty.domain.models.episodes.SingleEpisode
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestEpisodesRepository:EpisodesRepository {

    private val episodesFlow =
        MutableSharedFlow<List<SingleEpisode>>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    suspend fun sendEpisodes(episodes: List<SingleEpisode>) {
        episodesFlow.emit(episodes)
    }

    override suspend fun loadAllEpisodes(): Flow<List<SingleEpisode>> {
        TODO("Not yet implemented")
    }

    override suspend fun loadListEpisodesById(episodesId: List<Int>): Flow<List<SingleEpisode>> {
        return episodesFlow
    }

    override suspend fun filterEpisodes(searchQuery: HashMap<String, String>): Flow<List<SingleEpisode>> {
        TODO("Not yet implemented")
    }

    override suspend fun loadSingleEpisodeById(episodeId: Int): Flow<List<SingleEpisode>> {
        TODO("Not yet implemented")
    }
}