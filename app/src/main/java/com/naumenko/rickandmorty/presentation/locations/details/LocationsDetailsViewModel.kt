package com.naumenko.rickandmorty.presentation.locations.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naumenko.rickandmorty.base.StateSingle
import com.naumenko.rickandmorty.domain.RickMortyRepository
import com.naumenko.rickandmorty.domain.models.locations.SingleLocation
import com.naumenko.rickandmorty.presentation.mappers.toSingleCharacterListItem
import com.naumenko.rickandmorty.presentation.mappers.toSingleLocationListItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationsDetailsViewModel @Inject constructor(
    private val rickMortyRepository: RickMortyRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<StateSingle>(StateSingle.Loading)
    val state: StateFlow<StateSingle> = _state.asStateFlow()

    fun isConnect() = rickMortyRepository.isConnect()

    fun onFragmentLocationsDetailsCreated(locationId: Int) {
        viewModelScope.launch {
            _state.value = StateSingle.Loading
            rickMortyRepository.loadSingleLocationById(locationId)
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
            rickMortyRepository.loadListCharacterById(charactersId)
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