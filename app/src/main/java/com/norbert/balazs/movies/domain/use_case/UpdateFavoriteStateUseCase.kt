package com.norbert.balazs.movies.domain.use_case

import com.norbert.balazs.movies.domain.model.Movie
import com.norbert.balazs.movies.domain.repository.LocalMovieRepository
import javax.inject.Inject
import javax.inject.Named

class UpdateFavoriteStateUseCase @Inject constructor(
    @Named("LocalMovieRepository") private val localMovieRepository: LocalMovieRepository
) {
    suspend fun updateFavoriteState(movie: Movie, newValue: Boolean) {
        if (newValue) {
            localMovieRepository.addToFavorites(movie)
        } else {
            localMovieRepository.removeFromFavorites(movie.id)
        }
    }
}