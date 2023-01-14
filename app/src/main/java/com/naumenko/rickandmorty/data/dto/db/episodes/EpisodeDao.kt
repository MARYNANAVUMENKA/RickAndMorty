package com.naumenko.rickandmorty.data.dto.db.episodes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM episodes")
    suspend fun getAllEpisodes(): List<SingleEpisodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllEpisodes(episodes: List<SingleEpisodeEntity>)

    @Query("SELECT * FROM episodes WHERE id = :id")
    suspend fun getEpisodesById(id: Int): SingleEpisodeEntity

    @Query("SELECT * FROM episodes WHERE id IN (:id)")
    suspend fun getListEpisodesById(id: List<Int>): List<SingleEpisodeEntity>

    @Query("SELECT * FROM episodes WHERE (:name IS NULL OR name LIKE '%' || :name || '%')" + "AND (:number IS NULL OR episode LIKE :number)" )
    suspend fun getEpisodeByNameNumber(name:String?,number: String?): List<SingleEpisodeEntity>
}