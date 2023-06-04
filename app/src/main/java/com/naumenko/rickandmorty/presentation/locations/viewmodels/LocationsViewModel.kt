package com.naumenko.rickandmorty.presentation.locations.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naumenko.rickandmorty.base.Constants
import com.naumenko.rickandmorty.base.EMPTY
import com.naumenko.rickandmorty.base.State
import com.naumenko.rickandmorty.domain.CharactersRepository
import com.naumenko.rickandmorty.domain.LocationsRepository
import com.naumenko.rickandmorty.domain.models.locations.SingleLocation
import com.naumenko.rickandmorty.presentation.mappers.toSingleLocationListItem
import kotlinx.coroutines.flow.*

import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationsViewModel @Inject constructor(
    private val locationsRepository: LocationsRepository,
    private val charactersRepository: CharactersRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _searchByNameList = MutableStateFlow(listOf(String.EMPTY))
    private var querySearchLocationType: QuerySearchLocationType? = null

    private val _flowListLocations = MutableStateFlow<List<SingleLocation>>(emptyList())
    private val flowListLocations: StateFlow<List<SingleLocation>> =
        _flowListLocations.asStateFlow()

    init {
        onFragmentLocationsListCreated()
    }

    fun isConnect() = charactersRepository.isConnect()

    fun onFragmentLocationsListCreated() {
        viewModelScope.launch {
            _state.value = State.Loading
            locationsRepository.loadAllLocations()
                .collectLatest { list ->
                    _flowListLocations.value = list
                    _state.value = if (list.isNotEmpty()) {
                        State.Success(list.map {
                            it.toSingleLocationListItem()
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
            flowListLocations.combineTransform(_searchByNameList) { locationsFlow, searchBy ->
                val result =
                    locationsFlow.filter { it.name.lowercase().contains(searchBy[0].lowercase()) }
                emit(result)
            }.collectLatest { list ->
                _state.value = if (list.isNotEmpty()) {
                    State.Success(list.map {
                        it.toSingleLocationListItem()
                    })
                } else {
                    State.Error(message = "location not found")
                }
            }
        }
    }

    private fun onFilterClick(searchQuery: HashMap<String, String>) {
        viewModelScope.launch {
            _state.value = State.Loading
            locationsRepository.filterLocations(searchQuery)
                .collectLatest { list ->
                    _state.value = if (list.isNotEmpty()) {
                        State.Success(list.map {
                            it.toSingleLocationListItem()
                        })
                    } else {
                        State.Error(message = "locations not found")
                    }
                }
        }
    }

    fun saveQuerySearchLocationType(
        selectedLocationName: String,
        selectedLocationType: String,
        selectedLocationDimension: String,
    ) {
        querySearchLocationType = QuerySearchLocationType(
            selectedLocationName, selectedLocationType, selectedLocationDimension
        )
        onFilterClick(applyQueries())
    }

    private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        if (querySearchLocationType != null) {
            if (querySearchLocationType!!.selectedLocationName != String.EMPTY) {
                queries[Constants.QUERY_LOCATION_NAME] =
                    querySearchLocationType!!.selectedLocationName
            }
            if (querySearchLocationType!!.selectedLocationType != String.EMPTY) {
                queries[Constants.QUERY_LOCATION_TYPE] =
                    querySearchLocationType!!.selectedLocationType
            }
            if (querySearchLocationType!!.selectedLocationDimension != String.EMPTY) {
                queries[Constants.QUERY_LOCATION_DIMENSION] =
                    querySearchLocationType!!.selectedLocationDimension
            }
        }
        return queries
    }
}

data class QuerySearchLocationType(
    val selectedLocationName: String,
    val selectedLocationType: String,
    val selectedLocationDimension: String,
)