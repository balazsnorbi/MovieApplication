package com.norbert.balazs.movies.domain.model

import com.norbert.balazs.movies.data.local.FavoriteMovie

data class Movie(
    val id: Int,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseYear: String,
    val averageRating: Double,
    val isFavorite: Boolean
)

fun Movie.toFavoriteMovie(): FavoriteMovie {
    return FavoriteMovie(
        id,
        posterUrl,
        backdropUrl,
        releaseYear,
        averageRating
    )
}
