package com.norbert.balazs.movies.domain.use_case

import com.norbert.balazs.movies.common.BASE_IMAGE_URL
import com.norbert.balazs.movies.common.Resource
import com.norbert.balazs.movies.domain.model.Movie
import com.norbert.balazs.movies.domain.repository.LocalMovieRepository
import com.norbert.balazs.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class SearchMovieUseCase @Inject constructor(
    @Named("MovieRepository") private val movieRepository: MovieRepository,
    @Named("LocalMovieRepository") private val localMovieRepository: LocalMovieRepository
) {

    suspend fun invoke(query: String) = flow {
        try {
            localMovieRepository.getFavoriteMovies().collect { favoriteMovies ->
                emit(Resource.Loading())
                val listOfMovies = mutableListOf<Movie>()
                movieRepository.searchMovie(query).forEach { movieDto ->
                    var isFavorite = false
                    favoriteMovies.forEach { favoriteMovie ->
                        if (favoriteMovie.id == movieDto.id)
                            isFavorite = true
                    }
                    listOfMovies.add(
                        Movie(
                            movieDto.id,
                            BASE_IMAGE_URL + movieDto.poster_path,
                            BASE_IMAGE_URL + movieDto.backdrop_path,
                            movieDto.release_date?.split("-")?.get(0) ?: "",
                            movieDto.vote_average,
                            isFavorite
                        )
                    )
                }
                emit(Resource.Success(listOfMovies.toList()))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            emit(Resource.Error("Something went wrong while fetching favorites!"))
        }
    }
}