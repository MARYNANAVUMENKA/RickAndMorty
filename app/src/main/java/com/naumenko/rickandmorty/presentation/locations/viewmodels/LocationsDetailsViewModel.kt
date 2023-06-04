package com.naumenko.rickandmorty.presentation.locations.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naumenko.rickandmorty.base.StateSingle
import com.naumenko.rickandmorty.domain.CharactersRepository
import com.naumenko.rickandmorty.domain.LocationsRepository
import com.naumenko.rickandmorty.domain.models.locations.SingleLocation
import com.naumenko.rickandmorty.presentation.mappers.toSingleCharacterListItem
import com.naumenko.rickandmorty.presentation.mappers.toSingleLocationListItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationsDetailsViewModel @Inject constructor(
    private val locationsRepository: LocationsRepository,
    private val charactersRepository: CharactersRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<StateSingle>(StateSingle.Loading)
    val state: StateFlow<StateSingle> = _state.asStateFlow()

    fun isConnect() = charactersRepository.isConnect()

    fun onFragmentLocationsDetailsCreated(locationId: Int) {
        viewModelScope.launch {
            _state.value = StateSingle.Loading
            locationsRepository.loadSingleLocationById(locationId)
                .collectLatest { singleLocation ->
                    if (singleLocation.isNotEmpty() && singleLocation[0].residents.isNotEmpty()) {
                        val charactersId = mutableListOf<Int>()
                        singleLocation[0].residents.forEach { url ->
                            charactersId.add(url.substring(42).toInt())
                        }
                        loadListCharacter(singleLocation, charactersId)
                    } else if (singleLocation.isNotEmpty() && singleLocation[0].residents.isEmpty()) {
                        _state.value = StateSingle.Success(
                            singleLocation[0].toSingleLocationListItem(), null
                        )
                    } else {
                        _state.value = StateSingle.Error(message = "No Internet Connection.")
                    }
                }
        }
    }

    private fun loadListCharacter(singleLocation: List<SingleLocation>, charactersId: List<Int>) {
        viewModelScope.launch {
            charactersRepository.loadListCharacterById(charactersId)
                .collectLatest { listCharacters ->
                    _state.value = if (listCharacters.isNotEmpty()) {
                        StateSingle.Success(
                            singleLocation[0].toSingleLocationListItem(),
                            listCharacters.map { it.toSingleCharacterListItem() })
                    } else {
                        StateSingle.Error(message = "No Internet Connection.")
                    }
                }
        }
    }
}