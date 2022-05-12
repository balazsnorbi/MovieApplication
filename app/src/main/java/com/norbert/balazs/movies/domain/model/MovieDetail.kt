package com.norbert.balazs.movies.domain.model

data class MovieDetail(
    val id: Int,
    val title: String,
    val backdropUrl: String,
    val posterUrl: String,
    val tagLine: String,
    val releaseYear: String,
    val averageRating: Double,
    val voteCount: Int,
    val overview: String,
    val isFavorite: Boolean
)

fun MovieDetail.toMovie(): Movie {
    return Movie(
        id,
        posterUrl,
        backdropUrl,
        releaseYear,
        averageRating,
        isFavorite
    )
}
