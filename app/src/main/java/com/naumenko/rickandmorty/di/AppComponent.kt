package com.naumenko.rickandmorty.di

import android.app.Application
import com.naumenko.rickandmorty.presentation.characters.fragments.CharactersDetailsFragment
import com.naumenko.rickandmorty.presentation.characters.fragments.CharactersFilterBottomSheetFragment
import com.naumenko.rickandmorty.presentation.characters.fragments.CharactersListFragment
import com.naumenko.rickandmorty.presentation.MainActivity
import com.naumenko.rickandmorty.presentation.episodes.fragments.EpisodesDetailsFragment
import com.naumenko.rickandmorty.presentation.episodes.fragments.EpisodesFilterBottomSheetFragment
import com.naumenko.rickandmorty.presentation.episodes.fragments.EpisodesListFragment
import com.naumenko.rickandmorty.presentation.locations.fragments.LocationsDetailsFragment
import com.naumenko.rickandmorty.presentation.locations.fragments.LocationsFilterBottomSheetFragment
import com.naumenko.rickandmorty.presentation.locations.fragments.LocationsListFragment
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