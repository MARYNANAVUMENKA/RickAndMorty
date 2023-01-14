package com.naumenko.rickandmorty.presentation.episodes.list

import androidx.lifecycle.ViewModel
import com.naumenko.rickandmorty.base.State
import androidx.lifecycle.viewModelScope
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_EPISODE_NUMBER
import com.naumenko.rickandmorty.base.Constants.Companion.QUERY_EPISODE_NAME
import com.naumenko.rickandmorty.base.EMPTY
import com.naumenko.rickandmorty.domain.RickMortyRepository
import com.naumenko.rickandmorty.domain.models.episodes.SingleEpisode
import com.naumenko.rickandmorty.presentation.mappers.toSingleEpisodeListItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodesViewModel @Inject constructor(
    private val rickMortyRepository: RickMortyRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _searchByNameList = MutableStateFlow(listOf(String.EMPTY))
    private var querySearchEpisodeType: QuerySearchEpisodeType? = null

    private val _flowListEpisodes = MutableStateFlow<List<SingleEpisode>>(emptyList())
    private val flowListEpisodes: StateFlow<List<SingleEpisode>> = _flowListEpisodes.asStateFlow()

    init {
        onFragmentEpisodesListCreated()
    }

    fun isConnect() = rickMortyRepository.isConnect()

    fun onFragmentEpisodesListCreated() {
        viewModelScope.launch {
            _state.value = State.Loading
            rickMortyRepository.loadAllEpisodes()
                .collectLatest { list ->
                    _flowListEpisodes.value = list
                    _state.value = if (list.isNotEmpty()) {
                        State.Success(list.map {
                            it.toSingleEpisodeListItem()
                        })
                    } else {
                        State.Error(message = "No Internet Connection.")
                    }
                }
        }

    }

    fun onSearchToolbarClick(searchBy: String) {
        viewModelScope.launch {
            _searchByNameList.value = listOf(searchBy)
            flowListEpisodes.combineTransform(_searchByNameList) { episodesFlow, searchBy ->
                val result =
                    episodesFlow.filter { it.name.lowercase().contains(searchBy[0].lowercase()) }
                emit(result)
            }.collectLatest { list ->
                _state.value = if (list.isNotEmpty()) {
                    State.Success(list.map {
                        it.toSingleEpisodeListItem()
                    })
                } else {
                    State.Error(message = "Episodes not found")
                }
            }
        }
    }


    private fun onFilterClick(searchQuery: HashMap<String, String>) {
        viewModelScope.launch {
            _state.value = State.Loading
            rickMortyRepository.filterEpisodes(searchQuery)
                .collectLatest { list ->
                    _state.value = if (list.isNotEmpty()) {
                        State.Success(list.map {
                            it.toSingleEpisodeListItem()
                        })
                    } else {
                        State.Error(message = "Episodes not found")
                    }
                }
        }
    }

    fun saveQuerySearchEpisodeType(
        selectedEpisodeName: String,
        selectedEpisodeNumber: String,
    ) {
        querySearchEpisodeType = QuerySearchEpisodeType(
            selectedEpisodeName, selectedEpisodeNumber
        )
        onFilterClick(applyQueries())
    }

    private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        if (querySearchEpisodeType != null) {
            if (querySearchEpisodeType!!.selectedEpisodeName != String.EMPTY) {
                queries[QUERY_EPISODE_NAME] = querySearchEpisodeType!!.selectedEpisodeName
            }
            if (querySearchEpisodeType!!.selectedEpisodeNumber != String.EMPTY) {
                queries[QUERY_EPISODE_NUMBER] = querySearchEpisodeType!!.selectedEpisodeNumber
            }
        }
        return queries
    }
}

data class QuerySearchEpisodeType(
    val selectedEpisodeName: String,
    val selectedEpisodeNumber: String,
)