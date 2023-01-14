package com.naumenko.rickandmorty.presentation.episodes.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naumenko.rickandmorty.base.StateSingle
import com.naumenko.rickandmorty.domain.RickMortyRepository
import com.naumenko.rickandmorty.presentation.mappers.toSingleCharacterListItem
import com.naumenko.rickandmorty.presentation.mappers.toSingleEpisodeListItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodesDetailsViewModel @Inject constructor(
    private val rickMortyRepository: RickMortyRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<StateSingle>(StateSingle.Loading)
    val state: StateFlow<StateSingle> = _state.asStateFlow()

    fun isConnect() = rickMortyRepository.isConnect()

    fun onFragmentEpisodesDetailsCreated(episodeId: Int) {
        viewModelScope.launch {
            _state.value = StateSingle.Loading
            rickMortyRepository.loadSingleEpisodeById(episodeId)
                .collectLatest { singleEpisode ->
                    if (singleEpisode.isNotEmpty()) {
                        val charactersId = mutableListOf<Int>()
                        singleEpisode[0].characters.forEach { url ->
                            charactersId.add(url.substring(42).toInt())
                        }
                        rickMortyRepository.loadListCharacterById(charactersId)
                            .collectLatest { listCharacters ->
                                _state.value = if (listCharacters.isNotEmpty()) {
                                    StateSingle.Success(
                                        singleEpisode[0].toSingleEpisodeListItem(),
                                        listCharacters.map {
                                            it.toSingleCharacterListItem()
                                        })
                                } else {
                                    StateSingle.Error(message = "No Internet Connection.")
                                }
                            }
                    } else {
                        _state.value = StateSingle.Error(message = "No Internet Connection.")
                    }
                }
        }
    }
}