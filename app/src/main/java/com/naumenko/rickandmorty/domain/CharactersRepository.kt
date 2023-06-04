package com.naumenko.rickandmorty.domain

import com.naumenko.rickandmorty.domain.models.characters.SingleCharacter
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {

    fun isConnect(): Boolean

    suspend fun loadAllCharacters(): Flow<List<SingleCharacter>>

    suspend fun filterCharacters(searchQuery: HashMap<String, String>): Flow<List<SingleCharacter>>

    suspend fun loadSingleCharacterById(characterId: Int): Flow<List<SingleCharacter>>

    suspend fun loadListCharacterById(charactersId: List<Int>): Flow<List<SingleCharacter>>
}