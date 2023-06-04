package com.naumenko.rickandmorty.presentation.characters.viewmodels

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

class CharactersDetailsViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository,
    private val episodesRepository: EpisodesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<StateSingle>(StateSingle.Loading)
    val state: StateFlow<StateSingle> = _state.asStateFlow()

    fun isConnect() = charactersRepository.isConnect()

    fun onFragmentCharactersDetailsCreated(characterId: Int) {
        viewModelScope.launch {
            _state.value = StateSingle.Loading
            charactersRepository.loadSingleCharacterById(characterId)
                .collectLatest { singleCharacter ->
                    if (singleCharacter.isNotEmpty()) {
                        val episodesId = mutableListOf<Int>()
                        singleCharacter[0].episode.forEach { url ->
                            episodesId.add(url.substring(40).toInt())
                        }
                        episodesRepository.loadListEpisodesById(episodesId)
                            .collectLatest { listEpisodes ->
                                _state.value = if (listEpisodes.isNotEmpty()) {
                                    StateSingle.Success(
                                        singleCharacter[0].toSingleCharacterListItem(),
                                        listEpisodes.map {
                                            it.toSingleEpisodeListItem()
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