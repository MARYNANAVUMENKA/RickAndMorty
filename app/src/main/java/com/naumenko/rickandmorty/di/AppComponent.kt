package com.naumenko.rickandmorty.di

import android.app.Application
import com.naumenko.rickandmorty.presentation.characters.details.CharactersDetailsFragment
import com.naumenko.rickandmorty.presentation.characters.filter.CharactersFilterBottomSheetFragment
import com.naumenko.rickandmorty.presentation.characters.list.CharactersListFragment
import com.naumenko.rickandmorty.presentation.MainActivity
import com.naumenko.rickandmorty.presentation.episodes.details.EpisodesDetailsFragment
import com.naumenko.rickandmorty.presentation.episodes.filter.EpisodesFilterBottomSheetFragment
import com.naumenko.rickandmorty.presentation.episodes.list.EpisodesListFragment
import com.naumenko.rickandmorty.presentation.locations.details.LocationsDetailsFragment
import com.naumenko.rickandmorty.presentation.locations.filter.LocationsFilterBottomSheetFragment
import com.naumenko.rickandmorty.presentation.locations.list.LocationsListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppDataModule::class, ApiModule::class, ViewModelModule::class])
@Singleton
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(fragment: CharactersListFragment)
    fun inject(fragment: CharactersFilterBottomSheetFragment)
    fun inject(fragment: CharactersDetailsFragment)
    fun inject(fragment: LocationsListFragment)
    fun inject(fragment: LocationsFilterBottomSheetFragment)
    fun inject(fragment: LocationsDetailsFragment)
    fun inject(fragment: EpisodesListFragment)
    fun inject(fragment: EpisodesFilterBottomSheetFragment)
    fun inject(fragment: EpisodesDetailsFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}