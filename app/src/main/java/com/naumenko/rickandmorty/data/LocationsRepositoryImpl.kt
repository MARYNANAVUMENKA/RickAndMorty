package com.naumenko.rickandmorty.data

import android.util.Log
import com.naumenko.rickandmorty.base.Constants
import com.naumenko.rickandmorty.data.dto.api.ApiLocations
import com.naumenko.rickandmorty.data.dto.db.locations.LocationDao
import com.naumenko.rickandmorty.data.dto.toSingleLocation
import com.naumenko.rickandmorty.data.dto.toSingleLocationEntity
import com.naumenko.rickandmorty.domain.LocationsRepository
import com.naumenko.rickandmorty.domain.models.locations.SingleLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LocationsRepositoryImpl @Inject constructor(
    private val api: ApiLocations,
    private val locationDao: LocationDao,
) : LocationsRepository {

    override suspend fun loadAllLocations(): Flow<List<SingleLocation>> =
        flow<List<SingleLocation>> {
            val listLocations = mutableListOf<SingleLocation>()
            val pageSize = api.getAllLocations().infoLocationDTO?.pages
            if (pageSize != null) {
                for (page in 1..pageSize) {
                    listLocations.addAll(
                        api.getAllLocationsWithPage(page)
                            .singleLocationDTO.map { it.toSingleLocation() })
                }
            }
            emit(listLocations)
        }.onEach { list ->
            locationDao.saveAllLocations(list.map { it.toSingleLocationEntity() })
        }
            .catch { e ->
                e.localizedMessage?.let { Log.d("Network error", it) }
                if (locationDao.getAllLocations().isNotEmpty()) {
                    emit(locationDao.getAllLocations().map { it.toSingleLocation() })
                } else {
                    emit(emptyList())
                }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun filterLocations(searchQuery: HashMap<String, String>): Flow<List<SingleLocation>> =
        flow<List<SingleLocation>> {
            val listLocations = mutableListOf<SingleLocation>()
            val pageSize = api.filterLocations(searchQuery).infoLocationDTO?.pages
            if (pageSize != null) {
                for (page in 1..pageSize) {
                    searchQuery[Constants.QUERY_LOCATION_PAGE] = page.toString()
                    listLocations.addAll(
                        api.filterLocations(searchQuery)
                            .singleLocationDTO.map { it.toSingleLocation() })
                }
            }
            emit(listLocations)
        }
            .catch { e ->
                e.localizedMessage?.let { Log.d("Network error", it) }
                if (locationDao.getLocationByNameTypeDimension(
                        searchQuery[Constants.QUERY_LOCATION_NAME],
                        searchQuery[Constants.QUERY_LOCATION_TYPE],
                        searchQuery[Constants.QUERY_LOCATION_DIMENSION]
                    ).isNotEmpty()
                ) {
                    emit(
                        locationDao.getLocationByNameTypeDimension(
                            searchQuery[Constants.QUERY_LOCATION_NAME],
                            searchQuery[Constants.QUERY_LOCATION_TYPE],
                            searchQuery[Constants.QUERY_LOCATION_DIMENSION]
                        ).map { it.toSingleLocation() })
                } else {
                    emit(emptyList())
                }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun loadSingleLocationById(locationId: Int): Flow<List<SingleLocation>> =
        flow {
            val listSingleLocation = listOf(api.loadLocationById(locationId).toSingleLocation())
            emit(listSingleLocation)
        }.catch { e ->
            e.localizedMessage?.let { Log.d("Network error", it) }
            if (locationDao.getAllLocations().isNotEmpty()) {
                val listSingleLocation =
                    listOf(locationDao.getLocationById(locationId).toSingleLocation())
                emit(listSingleLocation)
            } else {
                emit(emptyList())
            }
        }
            .flowOn(Dispatchers.IO)
}