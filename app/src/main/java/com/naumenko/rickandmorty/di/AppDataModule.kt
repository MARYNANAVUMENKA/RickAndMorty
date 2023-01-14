package com.naumenko.rickandmorty.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.naumenko.rickandmorty.data.dto.api.ApiService
import com.naumenko.rickandmorty.base.ConnectivityChecker
import com.naumenko.rickandmorty.data.RickMortyRepositoryImpl
import com.naumenko.rickandmorty.data.dto.db.RickMortyDatabase
import com.naumenko.rickandmorty.data.dto.db.characters.CharacterDao
import com.naumenko.rickandmorty.data.dto.db.episodes.EpisodeDao
import com.naumenko.rickandmorty.data.dto.db.locations.LocationDao
import com.naumenko.rickandmorty.domain.RickMortyRepository
import dagger.Module
import dagger.Provides

@Module
class AppDataModule {

    @Provides
    fun provideAppContext(app: Application): Context = app.applicationContext

    @Provides
    fun provideConnectivityChecker(context: Context): ConnectivityChecker {
        return ConnectivityChecker(context)
    }

    @Provides
    fun provideRickMortyDatabase(context: Context): RickMortyDatabase {
        return Room.databaseBuilder(
            context,
            RickMortyDatabase::class.java,
            RickMortyDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideSingleCharacterDao(rickMortyDatabase: RickMortyDatabase): CharacterDao {
        return rickMortyDatabase.getCharacterDao()
    }

    @Provides
    fun provideSingleLocationDao(rickMortyDatabase: RickMortyDatabase): LocationDao {
        return rickMortyDatabase.getLocationDao()
    }

    @Provides
    fun provideSingleEpisodesDao(rickMortyDatabase: RickMortyDatabase): EpisodeDao {
        return rickMortyDatabase.getEpisodeDao()
    }

    @Provides
    fun provideRickMortyRepository(
        connectivityChecker: ConnectivityChecker,
        apiService: ApiService,
        characterDao: CharacterDao,
        locationDao: LocationDao,
        episodeDao: EpisodeDao
    ): RickMortyRepository {
        return RickMortyRepositoryImpl(connectivityChecker, apiService, characterDao,locationDao,episodeDao)
    }
}