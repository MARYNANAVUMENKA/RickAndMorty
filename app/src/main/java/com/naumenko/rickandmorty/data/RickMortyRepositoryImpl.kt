package com.naumenko.rickandmorty.data

import android.util.Log
import com.naumenko.rickandmorty.base.ConnectivityChecker
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_CHARACTER_GENDER
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_CHARACTER_NAME
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_CHARACTER_PAGE
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_CHARACTER_SPECIES
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_CHARACTER_STATUS
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_EPISODE_NAME
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_EPISODE_NUMBER
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_EPISODE_PAGE
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_LOCATION_DIMENSION
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_LOCATION_NAME
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_LOCATION_PAGE
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_LOCATION_TYPE
import com.naumenko.rickandmorty.data.dto.*
import com.naumenko.rickandmorty.data.dto.api.ApiService
import com.naumenko.rickandmorty.data.dto.db.characters.CharacterDao
import com.naumenko.rickandmorty.domain.models.characters.SingleCharacter
import com.naumenko.rickandmorty.domain.models.locations.SingleLocation
import com.naumenko.rickandmorty.data.dto.db.episodes.EpisodeDao
import com.naumenko.rickandmorty.data.dto.db.locations.LocationDao
import com.naumenko.rickandmorty.domain.RickMortyRepository
import com.naumenko.rickandmorty.domain.models.episodes.SingleEpisode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class RickMortyRepositoryImpl @Inject constructor(
    private val connectivityChecker: ConnectivityChecker,
    private val api: ApiService,
    private val characterDao: CharacterDao,
    private val locationDao: LocationDao,
    private val episodeDao: EpisodeDao,
) : RickMortyRepository {

    override fun isConnect(): Boolean = connectivityChecker.isConnect()

    override suspend fun loadAllCharacters(): Flow<List<SingleCharacter>> =
        flow<List<SingleCharacter>> {
            val listCharacters = mutableListOf<SingleCharacter>()
            val pageSize = api.getAllCharacters().body()?.info?.pages
            if (pageSize != null) {
                for (page in 1..pageSize) {
                    listCharacters.addAll(
                        api.getAllCharactersWithPage(page)
                            .body()!!.singleCharacterDTO.map { it.toSingleCharacter() })
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
            val pageSize = api.filterCharacters(searchQuery).body()?.info?.pages
            if (pageSize != null) {
                for (page in 1..pageSize) {
                    searchQuery[QUERY_CHARACTER_PAGE] = page.toString()
                    listCharacters.addAll(
                        api.filterCharacters(searchQuery)
                            .body()!!.singleCharacterDTO.map { it.toSingleCharacter() })
                }
            }
            emit(listCharacters)
        }
            .catch { e ->
                e.localizedMessage?.let { Log.d("Network error", it) }
                if (characterDao.getCharacterByNameStatusGenderSpecies(
                        searchQuery[QUERY_CHARACTER_NAME],
                        searchQuery[QUERY_CHARACTER_STATUS],
                        searchQuery[QUERY_CHARACTER_GENDER],
                        searchQuery[QUERY_CHARACTER_SPECIES]
                    ).isNotEmpty()
                ) {
                    emit(characterDao.getCharacterByNameStatusGenderSpecies(
                        searchQuery[QUERY_CHARACTER_NAME],
                        searchQuery[QUERY_CHARACTER_STATUS],
                        searchQuery[QUERY_CHARACTER_GENDER],
                        searchQuery[QUERY_CHARACTER_SPECIES]
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

    override suspend fun loadAllLocations(): Flow<List<SingleLocation>> =
        flow<List<SingleLocation>> {
            val listLocations = mutableListOf<SingleLocation>()
            val pageSize = api.getAllLocations().body()?.infoLocationDTO?.pages
            if (pageSize != null) {
                for (page in 1..pageSize) {
                    listLocations.addAll(
                        api.getAllLocationsWithPage(page)
                            .body()!!.singleLocationDTO.map { it.toSingleLocation() })
                }
            }
            emit(listLocations)
        }.onEach { list ->
            locationDao.saveAllLocations(list.map { it.toSingleLocationEntity() })
        }
            .catch { e ->
                e.localizedMessage?.let { Log.d("Network error", it) }
                if (locationDao.getAllLocations().isNotEmpty()) {
                    emit(locationDao.getAllLocations().map { it.toSingleLocation() })
                } else {
                    emit(emptyList())
                }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun filterLocations(searchQuery: HashMap<String, String>): Flow<List<SingleLocation>> =
        flow<List<SingleLocation>> {
            val listLocations = mutableListOf<SingleLocation>()
            val pageSize = api.filterLocations(searchQuery).body()?.infoLocationDTO?.pages
            if (pageSize != null) {
                for (page in 1..pageSize) {
                    searchQuery[QUERY_LOCATION_PAGE] = page.toString()
                    listLocations.addAll(
                        api.filterLocations(searchQuery)
                            .body()!!.singleLocationDTO.map { it.toSingleLocation() })
                }
            }
            emit(listLocations)
        }
            .catch { e ->
                e.localizedMessage?.let { Log.d("Network error", it) }
                if (locationDao.getLocationByNameTypeDimension(
                        searchQuery[QUERY_LOCATION_NAME],
                        searchQuery[QUERY_LOCATION_TYPE],
                        searchQuery[QUERY_LOCATION_DIMENSION]
                    ).isNotEmpty()
                ) {
                    emit(
                        locationDao.getLocationByNameTypeDimension(
                            searchQuery[QUERY_LOCATION_NAME],
                            searchQuery[QUERY_LOCATION_TYPE],
                            searchQuery[QUERY_LOCATION_DIMENSION]
                        ).map { it.toSingleLocation() })
                } else {
                    emit(emptyList())
                }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun loadSingleLocationById(locationId: Int): Flow<List<SingleLocation>> =
        flow {
            val listSingleLocation = listOf(api.loadLocationById(locationId).toSingleLocation())
            emit(listSingleLocation)
        }.catch { e ->
            e.localizedMessage?.let { Log.d("Network error", it) }
            if (locationDao.getAllLocations().isNotEmpty()) {
                val listSingleLocation =
                    listOf(locationDao.getLocationById(locationId).toSingleLocation())
                emit(listSingleLocation)
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

    override suspend fun loadAllEpisodes(): Flow<List<SingleEpisode>> =
        flow<List<SingleEpisode>> {
            val listEpisodes = mutableListOf<SingleEpisode>()
            val pageSize = api.getAllEpisodes().body()?.infoEpisodeDTO?.pages
            if (pageSize != null) {
                for (page in 1..pageSize) {
                    listEpisodes.addAll(
                        api.getAllEpisodesWithPage(page)
                            .body()!!.singleEpisodeDTO.map { it.toSingleEpisode() })
                }
            }
            emit(listEpisodes)
        }.onEach { list ->
            episodeDao.saveAllEpisodes(list.map { it.toSingleEpisodeEntity() })
        }
            .catch { e ->
                e.localizedMessage?.let { Log.d("Network error", it) }
                if (episodeDao.getAllEpisodes().isNotEmpty()) {
                    emit(episodeDao.getAllEpisodes().map { it.toSingleEpisode() })
                } else {
                    emit(emptyList())
                }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun loadListEpisodesById(episodesId: List<Int>): Flow<List<SingleEpisode>> =
        flow {
            emit(api.loadListEpisodesById(episodesId).map { it.toSingleEpisode() })
        }.catch { e ->
            e.localizedMessage?.let { Log.d("Network error", it) }
            emit(episodeDao.getListEpisodesById(episodesId).map { it.toSingleEpisode() })
        }
            .flowOn(Dispatchers.IO)

    override suspend fun filterEpisodes(searchQuery: HashMap<String, String>): Flow<List<SingleEpisode>> =
        flow<List<SingleEpisode>> {
            val listEpisodes = mutableListOf<SingleEpisode>()
            val pageSize = api.filterEpisodes(searchQuery).body()?.infoEpisodeDTO?.pages
            if (pageSize != null) {
                for (page in 1..pageSize) {
                    searchQuery[QUERY_EPISODE_PAGE] = page.toString()
                    listEpisodes.addAll(
                        api.filterEpisodes(searchQuery)
                            .body()!!.singleEpisodeDTO.map { it.toSingleEpisode() })
                }
            }
            emit(listEpisodes)
        }
            .catch { e ->
                e.localizedMessage?.let { Log.d("Network error", it) }
                if (episodeDao.getEpisodeByNameNumber(
                        searchQuery[QUERY_EPISODE_NAME],
                        searchQuery[QUERY_EPISODE_NUMBER]
                    ).isNotEmpty()
                ) {
                    emit(
                        episodeDao.getEpisodeByNameNumber(
                            searchQuery[QUERY_EPISODE_NAME],
                            searchQuery[QUERY_EPISODE_NUMBER]
                        ).map { it.toSingleEpisode() })
                } else {
                    emit(emptyList())
                }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun loadSingleEpisodeById(episodeId: Int): Flow<List<SingleEpisode>> =
        flow {
            val listSingleEpisode = listOf(api.loadEpisodeById(episodeId).toSingleEpisode())
            emit(listSingleEpisode)
        }.catch { e ->
            e.localizedMessage?.let { Log.d("Network error", it) }
            if (episodeDao.getAllEpisodes().isNotEmpty()) {
                val listSingleEpisode =
                    listOf(episodeDao.getEpisodesById(episodeId).toSingleEpisode())
                emit(listSingleEpisode)
            } else {
                emit(emptyList())
            }
        }
            .flowOn(Dispatchers.IO)

}