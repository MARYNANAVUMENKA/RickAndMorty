package com.naumenko.rickandmorty.data.dto.db.locations

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {

    @Query("SELECT * FROM locations")
    suspend fun getAllLocations(): List<SingleLocationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllLocations(locations: List<SingleLocationEntity>)

    @Query("SELECT * FROM locations WHERE id = :id")
    suspend fun getLocationById(id: Int): SingleLocationEntity

    @Query("SELECT * FROM locations WHERE (:name IS NULL OR name LIKE '%' || :name || '%')" + "AND (:type IS NULL OR type LIKE :type)" + "AND (:dimension IS NULL OR dimension LIKE :dimension)")
    suspend fun getLocationByNameTypeDimension(
        name: String?,
        type: String?,
        dimension: String?
    ): List<SingleLocationEntity>
}