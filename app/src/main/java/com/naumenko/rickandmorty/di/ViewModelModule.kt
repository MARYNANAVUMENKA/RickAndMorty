package com.naumenko.rickandmorty.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.naumenko.rickandmorty.presentation.characters.details.CharactersDetailsViewModel
import com.naumenko.rickandmorty.presentation.characters.list.CharactersViewModel
import com.naumenko.rickandmorty.presentation.episodes.details.EpisodesDetailsViewModel
import com.naumenko.rickandmorty.presentation.episodes.list.EpisodesViewModel
import com.naumenko.rickandmorty.presentation.locations.details.LocationsDetailsViewModel
import com.naumenko.rickandmorty.presentation.locations.list.LocationsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CharactersViewModel::class)
    fun bindCharactersViewModel(impl: CharactersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CharactersDetailsViewModel::class)
    fun bindCharactersDetailsViewModel(impl: CharactersDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationsViewModel::class)
    fun bindLocationsViewModel(impl: LocationsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationsDetailsViewModel::class)
    fun bindLocationsDetailsViewModel(impl: LocationsDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EpisodesViewModel::class)
    fun bindEpisodesViewModel(impl: EpisodesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EpisodesDetailsViewModel::class)
    fun bindEpisodesDetailsViewModel(impl: EpisodesDetailsViewModel): ViewModel

}
