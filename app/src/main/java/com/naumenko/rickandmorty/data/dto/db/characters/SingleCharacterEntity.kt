package com.naumenko.rickandmorty.data.dto.db.characters

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class SingleCharacterEntity(
    @PrimaryKey
    val id: Int,
    val created: String,
    val episode: List<String>,
    val gender: String,
    val image: String,
    @Embedded
    val location: LocationCharacterEntity,
    val name: String,
    @Embedded
    val origin: OriginCharacterEntity,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)

data class LocationCharacterEntity(
    @ColumnInfo(name = "location_name")
    val name: String,
    @ColumnInfo(name = "location_url")
    val url: String
)

data class OriginCharacterEntity(
    @ColumnInfo(name = "origin_name")
    val name: String,
    @ColumnInfo(name = "origin_url")
    val url: String
)