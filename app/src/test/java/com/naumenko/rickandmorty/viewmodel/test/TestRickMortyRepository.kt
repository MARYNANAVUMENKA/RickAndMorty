package com.naumenko.rickandmorty.viewmodel.test

import com.naumenko.rickandmorty.domain.RickMortyRepository
import com.naumenko.rickandmorty.domain.models.characters.SingleCharacter
import com.naumenko.rickandmorty.domain.models.episodes.SingleEpisode
import com.naumenko.rickandmorty.domain.models.locations.SingleLocation
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestRickMortyRepository : RickMortyRepository {

    private val charactersFlow =
        MutableSharedFlow<List<SingleCharacter>>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    private val episodesFlow =
        MutableSharedFlow<List<SingleEpisode>>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    suspend fun sendCharacters(characters: List<SingleCharacter>) {
        charactersFlow.emit(characters)
    }

    suspend fun sendEpisodes(episodes: List<SingleEpisode>) {
        episodesFlow.emit(episodes)
    }


    override fun isConnect(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun loadAllCharacters(): Flow<List<SingleCharacter>> {
        return charactersFlow
    }

    override suspend fun filterCharacters(searchQuery: HashMap<String, String>): Flow<List<SingleCharacter>> {
        return charactersFlow
    }

    override suspend fun loadSingleCharacterById(characterId: Int): Flow<List<SingleCharacter>> {
        return charactersFlow
    }

    override suspend fun loadAllLocations(): Flow<List<SingleLocation>> {
        TODO("Not yet implemented")
    }

    override suspend fun filterLocations(searchQuery: HashMap<String, String>): Flow<List<SingleLocation>> {
        TODO("Not yet implemented")
    }

    override suspend fun loadSingleLocationById(locationId: Int): Flow<List<SingleLocation>> {
        TODO("Not yet implemented")
    }

    override suspend fun loadListCharacterById(charactersId: List<Int>): Flow<List<SingleCharacter>> {
        return charactersFlow
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