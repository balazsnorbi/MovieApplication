package com.norbert.balazs.movies.domain.use_case

import com.norbert.balazs.movies.common.Resource
import com.norbert.balazs.movies.data.local.toMovie
import com.norbert.balazs.movies.domain.model.Movie
import com.norbert.balazs.movies.domain.repository.LocalMovieRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class GetFavoritesMoviesUseCase @Inject constructor(
    @Named("LocalMovieRepository") private val localMovieRepository: LocalMovieRepository
) {
    suspend fun invoke() = flow {
        try {
            localMovieRepository.getFavoriteMovies().collect { favoriteMovies ->
                emit(Resource.Loading())
                val movies = mutableListOf<Movie>()
                favoriteMovies.forEach { favorite ->
                    movies.add(favorite.toMovie())
                }
                emit(Resource.Success(movies.toList()))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Something went wrong while fetching favorites!"))
        }
    }
}