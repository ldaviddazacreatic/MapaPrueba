package com.example.mapaprueba.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_points")
data class FavoritePoint(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val isAlert: Boolean = false
)
