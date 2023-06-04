package com.naumenko.rickandmorty.data

import android.util.Log
import com.naumenko.rickandmorty.base.ConnectivityChecker
import com.naumenko.rickandmorty.base.Constants
import com.naumenko.rickandmorty.data.dto.api.ApiCharacters
import com.naumenko.rickandmorty.data.dto.db.characters.CharacterDao
import com.naumenko.rickandmorty.data.dto.toSingleCharacter
import com.naumenko.rickandmorty.data.dto.toSingleCharacterEntity
import com.naumenko.rickandmorty.domain.CharactersRepository
import com.naumenko.rickandmorty.domain.models.characters.SingleCharacter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val connectivityChecker: ConnectivityChecker,
    private val api: ApiCharacters,
    private val characterDao: CharacterDao,
):CharactersRepository {

    override fun isConnect(): Boolean = connectivityChecker.isConnect()

    override suspend fun loadAllCharacters(): Flow<List<SingleCharacter>> =
        flow<List<SingleCharacter>> {
            val listCharacters = mutableListOf<SingleCharacter>()
            val pageSize = api.getAllCharacters()?.info?.pages
            if (pageSize != null) {
                for (page in 1..pageSize) {
                    listCharacters.addAll(
                        api.getAllCharactersWithPage(page)
                            .singleCharacterDTO.map { it.toSingleCharacter() })
                }
            }
            emit(listCharacters)
        }.onEach { list ->
            characterDao.saveAllCharacters(list.map { it.toSingleCharacterEntity() })
        }
            .catch { e ->
                e.localizedMessage?.let { Log.d("Network error", it) }
                if (characterDao.getAllCharacters().isNotEmpty()) {
                    emit(characterDao.getAllCharacters().map { it.toSingleCharacter() })
                } else {
                    emit(emptyList())
                }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun filterCharacters(searchQuery: HashMap<String, String>): Flow<List<SingleCharacter>> =
        flow<List<SingleCharacter>> {
            val listCharacters = mutableListOf<SingleCharacter>()
            val pageSize = api.filterCharacters(searchQuery).info?.pages
            if (pageSize != null) {
                for (page in 1..pageSize) {
                    searchQuery[Constants.QUERY_CHARACTER_PAGE] = page.toString()
                    listCharacters.addAll(
                        api.filterCharacters(searchQuery)
                            .singleCharacterDTO.map { it.toSingleCharacter() })
                }
            }
            emit(listCharacters)
        }
            .catch { e ->
                e.localizedMessage?.let { Log.d("Network error", it) }
                if (characterDao.getCharacterByNameStatusGenderSpecies(
                        searchQuery[Constants.QUERY_CHARACTER_NAME],
                        searchQuery[Constants.QUERY_CHARACTER_STATUS],
                        searchQuery[Constants.QUERY_CHARACTER_GENDER],
                        searchQuery[Constants.QUERY_CHARACTER_SPECIES]
                    ).isNotEmpty()
                ) {
                    emit(characterDao.getCharacterByNameStatusGenderSpecies(
                        searchQuery[Constants.QUERY_CHARACTER_NAME],
                        searchQuery[Constants.QUERY_CHARACTER_STATUS],
                        searchQuery[Constants.QUERY_CHARACTER_GENDER],
                        searchQuery[Constants.QUERY_CHARACTER_SPECIES]
                    ).map { it.toSingleCharacter() })
                } else {
                    emit(emptyList())
                }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun loadSingleCharacterById(characterId: Int): Flow<List<SingleCharacter>> =
        flow {
            val listSingleCharacter = listOf(api.loadCharacterById(characterId).toSingleCharacter())
            emit(listSingleCharacter)
        }.catch { e ->
            e.localizedMessage?.let { Log.d("Network error", it) }
            if (characterDao.getAllCharacters().isNotEmpty()) {
                val listSingleCharacter =
                    listOf(characterDao.getCharacterById(characterId).toSingleCharacter())
                emit(listSingleCharacter)
            } else {
                emit(emptyList())
            }
        }
            .flowOn(Dispatchers.IO)

    override suspend fun loadListCharacterById(charactersId: List<Int>): Flow<List<SingleCharacter>> =
        flow {
            emit(api.loadListCharactersById(charactersId).map { it.toSingleCharacter() })
        }.catch { e ->
            e.localizedMessage?.let { Log.d("Network error", it) }
            emit(characterDao.getListCharactersById(charactersId).map { it.toSingleCharacter() })
        }
            .flowOn(Dispatchers.IO)
}