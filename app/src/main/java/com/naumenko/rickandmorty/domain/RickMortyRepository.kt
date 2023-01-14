package com.naumenko.rickandmorty.domain

import com.naumenko.rickandmorty.domain.models.characters.SingleCharacter
import com.naumenko.rickandmorty.domain.models.episodes.SingleEpisode
import com.naumenko.rickandmorty.domain.models.locations.SingleLocation
import kotlinx.coroutines.flow.Flow

interface RickMortyRepository {

    fun isConnect(): Boolean

    suspend fun loadAllCharacters(): Flow<List<SingleCharacter>>

    suspend fun filterCharacters(searchQuery: HashMap<String, String>): Flow<List<SingleCharacter>>

    suspend fun loadSingleCharacterById(characterId: Int): Flow<List<SingleCharacter>>

    suspend fun loadAllLocations(): Flow<List<SingleLocation>>

    suspend fun filterLocations(searchQuery: HashMap<String, String>): Flow<List<SingleLocation>>

    suspend fun loadSingleLocationById(locationId: Int): Flow<List<SingleLocation>>

    suspend fun loadListCharacterById(charactersId: List<Int>): Flow<List<SingleCharacter>>

    suspend fun loadAllEpisodes(): Flow<List<SingleEpisode>>

    suspend fun loadListEpisodesById(episodesId: List<Int>): Flow<List<SingleEpisode>>

    suspend fun filterEpisodes(searchQuery: HashMap<String, String>): Flow<List<SingleEpisode>>

    suspend fun loadSingleEpisodeById(episodeId: Int): Flow<List<SingleEpisode>>

}