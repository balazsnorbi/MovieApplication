package com.norbert.balazs.movies.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.norbert.balazs.movies.domain.model.Movie

@Entity(tableName = "favorites_table")
data class FavoriteMovie(
    @PrimaryKey val id: Int,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseYear: String,
    val averageRating: Double
)

fun FavoriteMovie.toMovie(): Movie {
    return Movie(
        id,
        posterUrl,
        backdropUrl,
        releaseYear,
        averageRating,
        true
    )
}