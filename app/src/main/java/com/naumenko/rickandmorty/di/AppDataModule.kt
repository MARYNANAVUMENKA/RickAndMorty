package com.naumenko.rickandmorty.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.naumenko.rickandmorty.base.ConnectivityChecker
import com.naumenko.rickandmorty.data.CharactersRepositoryImpl
import com.naumenko.rickandmorty.data.EpisodesRepositoryImpl
import com.naumenko.rickandmorty.data.LocationsRepositoryImpl
import com.naumenko.rickandmorty.data.dto.api.ApiCharacters
import com.naumenko.rickandmorty.data.dto.api.ApiEpisodes
import com.naumenko.rickandmorty.data.dto.api.ApiLocations
import com.naumenko.rickandmorty.data.dto.db.RickMortyDatabase
import com.naumenko.rickandmorty.data.dto.db.characters.CharacterDao
import com.naumenko.rickandmorty.data.dto.db.episodes.EpisodeDao
import com.naumenko.rickandmorty.data.dto.db.locations.LocationDao
import com.naumenko.rickandmorty.domain.CharactersRepository
import com.naumenko.rickandmorty.domain.EpisodesRepository
import com.naumenko.rickandmorty.domain.LocationsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppDataModule {

    @Singleton
    @Provides
    fun provideAppContext(app: Application): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideConnectivityChecker(context: Context): ConnectivityChecker {
        return ConnectivityChecker(context)
    }

    @Singleton
    @Provides
    fun provideRickMortyDatabase(context: Context): RickMortyDatabase {
        return Room.databaseBuilder(
            context,
            RickMortyDatabase::class.java,
            RickMortyDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideSingleCharacterDao(rickMortyDatabase: RickMortyDatabase): CharacterDao {
        return rickMortyDatabase.getCharacterDao()
    }

    @Singleton
    @Provides
    fun provideSingleLocationDao(rickMortyDatabase: RickMortyDatabase): LocationDao {
        return rickMortyDatabase.getLocationDao()
    }

    @Singleton
    @Provides
    fun provideSingleEpisodesDao(rickMortyDatabase: RickMortyDatabase): EpisodeDao {
        return rickMortyDatabase.getEpisodeDao()
    }

    @Singleton
    @Provides
    fun provideCharactersRepository(
        connectivityChecker: ConnectivityChecker,
        apiService: ApiCharacters,
        characterDao: CharacterDao,
    ): CharactersRepository{
        return CharactersRepositoryImpl(connectivityChecker, apiService, characterDao)
    }

    @Singleton
    @Provides
    fun provideLocationsRepository(
        apiService: ApiLocations,
        locationDao: LocationDao,
    ): LocationsRepository {
        return LocationsRepositoryImpl(apiService, locationDao)
    }

    @Singleton
    @Provides
    fun provideEpisodesRepository(
        apiService: ApiEpisodes,
        episodeDao: EpisodeDao
    ): EpisodesRepository {
        return EpisodesRepositoryImpl(apiService, episodeDao)
    }
}