package com.naumenko.rickandmorty.presentation.episodes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naumenko.rickandmorty.base.StateSingle
import com.naumenko.rickandmorty.domain.CharactersRepository
import com.naumenko.rickandmorty.domain.EpisodesRepository
import com.naumenko.rickandmorty.presentation.mappers.toSingleCharacterListItem
import com.naumenko.rickandmorty.presentation.mappers.toSingleEpisodeListItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodesDetailsViewModel @Inject constructor(
    private val episodesRepository: EpisodesRepository,
    private val charactersRepository: CharactersRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<StateSingle>(StateSingle.Loading)
    val state: StateFlow<StateSingle> = _state.asStateFlow()

    fun isConnect() = charactersRepository.isConnect()

    fun onFragmentEpisodesDetailsCreated(episodeId: Int) {
        viewModelScope.launch {
            _state.value = StateSingle.Loading
            episodesRepository.loadSingleEpisodeById(episodeId)
                .collectLatest { singleEpisode ->
                    if (singleEpisode.isNotEmpty()) {
                        val charactersId = mutableListOf<Int>()
                        singleEpisode[0].characters.forEach { url ->
                            charactersId.add(url.substring(42).toInt())
                        }
                        charactersRepository.loadListCharacterById(charactersId)
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