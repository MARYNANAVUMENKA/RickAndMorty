package com.naumenko.rickandmorty.data.dto.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.naumenko.rickandmorty.data.dto.db.RickMortyDatabase.Companion.DATABASE_VERSION
import com.naumenko.rickandmorty.data.dto.db.characters.CharacterConverter
import com.naumenko.rickandmorty.data.dto.db.characters.CharacterDao
import com.naumenko.rickandmorty.data.dto.db.characters.SingleCharacterEntity
import com.naumenko.rickandmorty.data.dto.db.episodes.EpisodeDao
import com.naumenko.rickandmorty.data.dto.db.episodes.SingleEpisodeEntity
import com.naumenko.rickandmorty.data.dto.db.locations.LocationDao
import com.naumenko.rickandmorty.data.dto.db.locations.SingleLocationEntity

@Database(
    entities = [SingleCharacterEntity::class, SingleLocationEntity::class, SingleEpisodeEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(CharacterConverter::class)
abstract class RickMortyDatabase : RoomDatabase() {

    abstract fun getCharacterDao(): CharacterDao

    abstract fun getLocationDao(): LocationDao

    abstract fun getEpisodeDao(): EpisodeDao

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "rick_morty_database"
    }
}