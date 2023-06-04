package com.naumenko.rickandmorty.viewmodel.test

import app.cash.turbine.test
import com.naumenko.rickandmorty.base.State
import com.naumenko.rickandmorty.domain.models.characters.LocationCharacter
import com.naumenko.rickandmorty.domain.models.characters.OriginCharacter
import com.naumenko.rickandmorty.presentation.characters.viewmodels.CharactersViewModel
import com.naumenko.rickandmorty.presentation.characters.models.SingleCharacterListItem
import com.naumenko.rickandmorty.presentation.mappers.toSingleCharacterListItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CharactersViewModelTest : ViewModelTest() {

    private val repository = TestCharactersRepository()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when onFragmentCharactersListCreated(), state loading, repository emit list and then state success `() =
        runTest {
            val viewModel = CharactersViewModel(repository)
            val characters = listOf(
                createSingleCharacters(
                    id = 1,
                    name = "Rick",
                    created = "2017-11-04",
                    episode = listOf("episode1"),
                    gender = "male",
                    image = "image",
                    locationCharacter = LocationCharacter("earth", ""),
                    originCharacter = OriginCharacter("earth", ""),
                    species = "human",
                    status = "alive",
                    type = "",
                    url = ""
                ), createSingleCharacters(
                    id = 2,
                    name = "Morty",
                    created = "2017-11-04",
                    episode = listOf("episode1"),
                    gender = "male",
                    image = "image",
                    locationCharacter = LocationCharacter("earth", ""),
                    originCharacter = OriginCharacter("earth", ""),
                    species = "human",
                    status = "alive",
                    type = "",
                    url = ""
                )
            )

            viewModel.state.test {
                val firstItem = awaitItem()
                assertEquals(State.Loading, firstItem)
                repository.sendCharacters(characters)

                repository.loadAllCharacters()

                val secondItem = awaitItem()
                assert(secondItem is State.Success)
                assertEquals(
                    characters.map { it.toSingleCharacterListItem() },
                    secondItem.list as List<SingleCharacterListItem>?
                )
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when onFragmentCharactersListCreated(), state loading, repository emit emptyList and then state error `() =
        runTest {
            val viewModel = CharactersViewModel(repository)
            val message = "No Internet Connection."

            viewModel.state.test {
                val firstItem = awaitItem()
                assertEquals(State.Loading, firstItem)
                repository.sendCharacters(emptyList())

                repository.loadAllCharacters()

                val secondItem = awaitItem()
                assert(secondItem is State.Error)
                assertEquals(message, secondItem.message)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when onFilterClick(), state loading, repository emit filter list and then state success `() =
        runTest {
            val viewModel = CharactersViewModel(repository)
            val characters = listOf(
                createSingleCharacters(
                    id = 1,
                    name = "Rick",
                    created = "2017-11-04",
                    episode = listOf("episode1"),
                    gender = "male",
                    image = "image",
                    locationCharacter = LocationCharacter("earth", ""),
                    originCharacter = OriginCharacter("earth", ""),
                    species = "human",
                    status = "alive",
                    type = "",
                    url = ""
                ), createSingleCharacters(
                    id = 2,
                    name = "Morty",
                    created = "2017-11-04",
                    episode = listOf("episode1"),
                    gender = "male",
                    image = "image",
                    locationCharacter = LocationCharacter("earth", ""),
                    originCharacter = OriginCharacter("earth", ""),
                    species = "human",
                    status = "alive",
                    type = "",
                    url = ""
                )
            )

            viewModel.state.test {
                val firstItem = awaitItem()
                assertEquals(State.Loading, firstItem)
                repository.sendCharacters(characters)

                repository.filterCharacters(viewModel.applyQueries())

                val secondItem = awaitItem()
                assert(secondItem is State.Success)
                assertEquals(
                    characters.map { it.toSingleCharacterListItem() },
                    secondItem.list as List<SingleCharacterListItem>?
                )
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when onFilterClick(), state loading, repository emit emptyList and then state error `() =
        runTest {
            val viewModel = CharactersViewModel(repository)
            val message1 = "No Internet Connection."

            viewModel.state.test {
                val firstItem = awaitItem()
                assertEquals(State.Loading, firstItem)

                repository.sendCharacters(emptyList())
                repository.filterCharacters(viewModel.applyQueries())
                val secondItem = awaitItem()
                assert(secondItem is State.Error)
                assertEquals(message1, secondItem.message)
            }
        }
}