package com.naumenko.rickandmorty.viewmodel.test

import app.cash.turbine.test
import com.naumenko.rickandmorty.base.StateSingle
import com.naumenko.rickandmorty.domain.models.characters.LocationCharacter
import com.naumenko.rickandmorty.domain.models.characters.OriginCharacter
import com.naumenko.rickandmorty.presentation.characters.details.CharactersDetailsViewModel
import com.naumenko.rickandmorty.presentation.characters.models.SingleCharacterListItem
import com.naumenko.rickandmorty.presentation.episodes.models.SingleEpisodeListItem
import com.naumenko.rickandmorty.presentation.mappers.toSingleCharacterListItem
import com.naumenko.rickandmorty.presentation.mappers.toSingleEpisodeListItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class CharactersDetailsViewModelTest : ViewModelTest() {

    private val repository = TestRickMortyRepository()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when onFragmentCharactersDetailsCreated, state loading, repository emit emptyList and then state error `() =
        runTest {
            val viewModel = CharactersDetailsViewModel(repository)
            val message = "No Internet Connection."

            viewModel.state.test {
                val firstItem = awaitItem()
                Assert.assertEquals(StateSingle.Loading, firstItem)

                repository.sendCharacters(emptyList())
                viewModel.onFragmentCharactersDetailsCreated(1)
                val secondItem = awaitItem()
                assert(secondItem is StateSingle.Error)
                Assert.assertEquals(message, secondItem.message)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when onFragmentCharactersDetailsCreated, state loading, repository emit list and then state success  `() =
        runTest {
            val viewModel = CharactersDetailsViewModel(repository)
            val characters = listOf(
                createSingleCharacters(
                    id = 1,
                    name = "Rick",
                    created = "2017-11-04",
                    episode = listOf("https://rickandmortyapi.com/api/episode/27"),
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
            val episodes = listOf(
                createSingleEpisodes(
                    airDate = "12.05.2019",
                    characters = listOf("characters"),
                    created = "12.05.2019",
                    episode = "episode",
                    id = 2,
                    name = "Pilot",
                    url = "url"
                )
            )

            viewModel.state.test {
                val firstItem = awaitItem()
                Assert.assertEquals(StateSingle.Loading, firstItem)

                repository.sendCharacters(characters)
                repository.sendEpisodes(episodes)
                viewModel.onFragmentCharactersDetailsCreated(1)

                val secondItem = awaitItem()
                assert(secondItem is StateSingle.Success)
                Assert.assertEquals(
                    episodes.map { it.toSingleEpisodeListItem() },
                    secondItem.list as List<SingleEpisodeListItem>?
                )
                Assert.assertEquals(
                    characters[0].toSingleCharacterListItem(),
                    secondItem.single as SingleCharacterListItem
                )
            }
        }
}