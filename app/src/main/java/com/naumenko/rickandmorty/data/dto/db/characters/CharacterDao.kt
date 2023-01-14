package com.naumenko.rickandmorty.data.dto.db.characters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters")
    suspend fun getAllCharacters(): List<SingleCharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllCharacters(characters: List<SingleCharacterEntity>)

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): SingleCharacterEntity

    @Query("SELECT * FROM characters WHERE id IN (:id)")
    suspend fun getListCharactersById(id: List<Int>): List<SingleCharacterEntity>

    @Query("SELECT * FROM characters WHERE (:name IS NULL OR name LIKE '%' || :name || '%')" + "AND (:status IS NULL OR status LIKE :status)" + "AND (:gender IS NULL OR gender LIKE :gender)" + "AND (:species IS NULL OR species LIKE :species)")
    suspend fun getCharacterByNameStatusGenderSpecies(
        name: String?,
        status: String?,
        gender: String?,
        species: String?
    ): List<SingleCharacterEntity>

    @Query("DELETE from characters")
    suspend fun deleteALLCharacters()
}