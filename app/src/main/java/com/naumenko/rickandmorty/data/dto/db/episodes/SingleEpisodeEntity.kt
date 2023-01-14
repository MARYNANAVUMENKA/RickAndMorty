package com.naumenko.rickandmorty.data.dto.db.episodes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episodes")
data class SingleEpisodeEntity(
    val airDate: String,
    val characters: List<String>,
    val created: String,
    val episode: String,
    @PrimaryKey
    val id: Int,
    val name: String,
    val url: String
)