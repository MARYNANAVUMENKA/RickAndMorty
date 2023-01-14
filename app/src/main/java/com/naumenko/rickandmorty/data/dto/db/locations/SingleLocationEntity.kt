package com.naumenko.rickandmorty.data.dto.db.locations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class SingleLocationEntity(
    val created: String,
    val dimension: String,
    @PrimaryKey
    val id: Int,
    val name: String,
    val residents: List<String>,
    val type: String,
    val url: String
)