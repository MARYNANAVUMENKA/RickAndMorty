package com.naumenko.rickandmorty.presentation

import androidx.fragment.app.Fragment

fun Fragment.navigator() = requireActivity() as Navigation

interface Navigation {

    fun onPressedCharactersList()

    fun onPressedCharacterDetails(characterId: Int)

    fun onPressedCharactersFilter()

    fun onPressedLocationsList()

    fun onPressedLocationsDetails(locationId: Int)

    fun onPressedLocationsFilter()

    fun onPressedEpisodesList()

    fun onPressedEpisodesDetails(episodeId: Int)

    fun onPressedEpisodesFilter()

    fun goBack()

}