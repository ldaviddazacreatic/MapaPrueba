package com.example.mapaprueba.data

import androidx.room.*

@Dao
interface FavoritePointDao {
    @Query("SELECT * FROM favorite_points")
    suspend fun getAll(): List<FavoritePoint>

    @Insert
    suspend fun insert(point: FavoritePoint)

    @Delete
    suspend fun delete(point: FavoritePoint)
}
