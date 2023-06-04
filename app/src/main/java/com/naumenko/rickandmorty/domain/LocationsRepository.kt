package com.naumenko.rickandmorty.domain

import com.naumenko.rickandmorty.domain.models.locations.SingleLocation
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    suspend fun loadAllLocations(): Flow<List<SingleLocation>>

    suspend fun filterLocations(searchQuery: HashMap<String, String>): Flow<List<SingleLocation>>

    suspend fun loadSingleLocationById(locationId: Int): Flow<List<SingleLocation>>
}