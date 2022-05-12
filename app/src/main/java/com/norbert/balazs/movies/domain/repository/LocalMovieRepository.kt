package com.norbert.balazs.movies.domain.repository

import com.norbert.balazs.movies.data.local.FavoriteMovie
import com.norbert.balazs.movies.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface LocalMovieRepository {

    suspend fun getFavoriteMovieById(movieId: Int): FavoriteMovie?

    suspend fun addToFavorites(movie: Movie)

    suspend fun removeFromFavorites(movieId: Int)

    fun getFavoriteMovies(): Flow<List<FavoriteMovie>>
}