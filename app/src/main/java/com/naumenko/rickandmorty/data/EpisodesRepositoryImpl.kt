package com.naumenko.rickandmorty.data

import android.util.Log
import com.naumenko.rickandmorty.base.Constants
import com.naumenko.rickandmorty.data.dto.api.ApiEpisodes
import com.naumenko.rickandmorty.data.dto.db.episodes.EpisodeDao
import com.naumenko.rickandmorty.data.dto.toSingleEpisode
import com.naumenko.rickandmorty.data.dto.toSingleEpisodeEntity
import com.naumenko.rickandmorty.domain.EpisodesRepository
import com.naumenko.rickandmorty.domain.models.episodes.SingleEpisode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class EpisodesRepositoryImpl @Inject constructor(
    private val api: ApiEpisodes,
    private val episodeDao: EpisodeDao,
) : EpisodesRepository {

    override suspend fun loadAllEpisodes(): Flow<List<SingleEpisode>> =
        flow<List<SingleEpisode>> {
            val listEpisodes = mutableListOf<SingleEpisode>()
            val pageSize = api.getAllEpisodes().infoEpisodeDTO?.pages
            if (pageSize != null) {
                for (page in 1..pageSize) {
                    listEpisodes.addAll(
                        api.getAllEpisodesWithPage(page)
                            .singleEpisodeDTO.map { it.toSingleEpisode() })
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
            val pageSize = api.filterEpisodes(searchQuery).infoEpisodeDTO?.pages
            if (pageSize != null) {
                for (page in 1..pageSize) {
                    searchQuery[Constants.QUERY_EPISODE_PAGE] = page.toString()
                    listEpisodes.addAll(
                        api.filterEpisodes(searchQuery)
                            .singleEpisodeDTO.map { it.toSingleEpisode() })
                }
            }
            emit(listEpisodes)
        }
            .catch { e ->
                e.localizedMessage?.let { Log.d("Network error", it) }
                if (episodeDao.getEpisodeByNameNumber(
                        searchQuery[Constants.QUERY_EPISODE_NAME],
                        searchQuery[Constants.QUERY_EPISODE_NUMBER]
                    ).isNotEmpty()
                ) {
                    emit(
                        episodeDao.getEpisodeByNameNumber(
                            searchQuery[Constants.QUERY_EPISODE_NAME],
                            searchQuery[Constants.QUERY_EPISODE_NUMBER]
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