package com.naumenko.rickandmorty.viewmodel.test

import com.naumenko.rickandmorty.domain.CharactersRepository
import com.naumenko.rickandmorty.domain.models.characters.SingleCharacter
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestCharactersRepository : CharactersRepository {

    private val charactersFlow =
        MutableSharedFlow<List<SingleCharacter>>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    suspend fun sendCharacters(characters: List<SingleCharacter>) {
        charactersFlow.emit(characters)
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

    override suspend fun loadListCharacterById(charactersId: List<Int>): Flow<List<SingleCharacter>> {
        return charactersFlow
    }
}