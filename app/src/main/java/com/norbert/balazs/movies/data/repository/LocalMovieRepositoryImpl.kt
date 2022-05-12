package com.norbert.balazs.movies.data.repository

import com.norbert.balazs.movies.data.local.FavoriteMovie
import com.norbert.balazs.movies.data.local.MovieDatabase
import com.norbert.balazs.movies.domain.model.Movie
import com.norbert.balazs.movies.domain.model.toFavoriteMovie
import com.norbert.balazs.movies.domain.repository.LocalMovieRepository
import javax.inject.Inject
import javax.inject.Named

class LocalMovieRepositoryImpl @Inject constructor(
    @Named("MovieDatabase") private val movieDatabase: MovieDatabase
) : LocalMovieRepository {


    override suspend fun getFavoriteMovieById(movieId: Int): FavoriteMovie? {
        val result = movieDatabase.movieDao().getFavoriteMovieById(movieId)
        if (result.isNotEmpty()) {
            return result[0]
        }
        return null
    }

    override suspend fun addToFavorites(movie: Movie) {
        movieDatabase.movieDao().addToFavorites(listOf(movie.toFavoriteMovie()))
    }

    override suspend fun removeFromFavorites(movieId: Int) {
        movieDatabase.movieDao().removeFromFavorites(movieId)
    }

    override fun getFavoriteMovies() = movieDatabase.movieDao().getFavorites()
}