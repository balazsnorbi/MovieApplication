package com.norbert.balazs.movies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favoritesMovies: List<FavoriteMovie>)

    @Query("SELECT * FROM favorites_table WHERE id=:id")
    suspend fun getFavoriteMovieById(id: Int): List<FavoriteMovie>

    @Query("DELETE FROM favorites_table WHERE id=:id")
    suspend fun removeFromFavorites(id: Int)

    @Query("SELECT * FROM favorites_table")
    fun getFavorites(): Flow<List<FavoriteMovie>>
}