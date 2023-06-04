package com.naumenko.rickandmorty.presentation.characters.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naumenko.rickandmorty.base.Constants
import com.naumenko.rickandmorty.base.EMPTY
import com.naumenko.rickandmorty.base.State
import com.naumenko.rickandmorty.domain.CharactersRepository
import com.naumenko.rickandmorty.domain.models.characters.SingleCharacter
import com.naumenko.rickandmorty.presentation.mappers.toSingleCharacterListItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _searchByNameList = MutableStateFlow(listOf(String.EMPTY))
    private var querySearchCharacterType: QuerySearchCharacterType? = null

    private val _flowListCharacters = MutableStateFlow<List<SingleCharacter>>(emptyList())
    private val flowListCharacters: StateFlow<List<SingleCharacter>> =
        _flowListCharacters.asStateFlow()


    init {
        onFragmentCharactersListCreated()
    }

    fun isConnect() = charactersRepository.isConnect()

    fun onFragmentCharactersListCreated() {
        viewModelScope.launch {
            _state.value = State.Loading
            charactersRepository.loadAllCharacters()
                .collectLatest { list ->
                    _flowListCharacters.value = list
                    _state.value = if (list.isNotEmpty()) {
                        State.Success(list.map {
                            it.toSingleCharacterListItem()
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
            flowListCharacters.combineTransform(_searchByNameList) { characterFlow, searchBy ->
                val result =
                    characterFlow.filter { it.name.lowercase().contains(searchBy[0].lowercase()) }
                emit(result)
            }.collectLatest { list ->
                _state.value = if (list.isNotEmpty()) {
                    State.Success(list.map {
                        it.toSingleCharacterListItem()
                    })
                } else {
                    State.Error(message = "Characters not found")
                }
            }
        }
    }


    private fun onFilterClick(searchQuery: HashMap<String, String>) {
        viewModelScope.launch {
            _state.value = State.Loading
            charactersRepository.filterCharacters(searchQuery)
                .collectLatest { list ->
                    _state.value = if (list.isNotEmpty()) {
                        State.Success(list.map {
                            it.toSingleCharacterListItem()
                        })
                    } else {
                        State.Error(message = "Characters not found")
                    }
                }
        }
    }

    fun saveQuerySearchType(
        selectedName: String,
        selectedStatusType: String,
        selectedGenderType: String,
        selectedSpeciesType: String,

        ) {
        querySearchCharacterType = QuerySearchCharacterType(
            selectedName,
            selectedStatusType,
            selectedGenderType,
            selectedSpeciesType,
        )
        onFilterClick(applyQueries())
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        if (querySearchCharacterType != null) {
            if (querySearchCharacterType!!.selectedName != String.EMPTY) {
                queries[Constants.QUERY_CHARACTER_NAME] = querySearchCharacterType!!.selectedName
            }
            if (querySearchCharacterType!!.selectedStatusType != String.EMPTY) {
                queries[Constants.QUERY_CHARACTER_STATUS] =
                    querySearchCharacterType!!.selectedStatusType
            }
            if (querySearchCharacterType!!.selectedGenderType != String.EMPTY) {
                queries[Constants.QUERY_CHARACTER_GENDER] =
                    querySearchCharacterType!!.selectedGenderType
            }
            if (querySearchCharacterType!!.selectedSpeciesType != String.EMPTY) {
                queries[Constants.QUERY_CHARACTER_SPECIES] =
                    querySearchCharacterType!!.selectedSpeciesType
            }
        }
        return queries
    }
}

data class QuerySearchCharacterType(
    val selectedName: String,
    val selectedStatusType: String,
    val selectedGenderType: String,
    val selectedSpeciesType: String,
)