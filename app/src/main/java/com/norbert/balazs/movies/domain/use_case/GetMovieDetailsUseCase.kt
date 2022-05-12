package com.norbert.balazs.movies.domain.use_case

import com.norbert.balazs.movies.common.BASE_IMAGE_URL
import com.norbert.balazs.movies.common.Resource
import com.norbert.balazs.movies.domain.model.MovieDetail
import com.norbert.balazs.movies.domain.repository.LocalMovieRepository
import com.norbert.balazs.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class GetMovieDetailsUseCase @Inject constructor(
    @Named("MovieRepository") private val movieRepository: MovieRepository,
    @Named("LocalMovieRepository") private val localMovieRepository: LocalMovieRepository
) {
    suspend fun invoke(movieId: Int) = flow {
        try {
            localMovieRepository.getFavoriteMovies().collect { favoriteMovies ->
                val movieDetailsDto = movieRepository.getMovieDetails(movieId)
                var isFavorite = false
                favoriteMovies.forEach { favoriteMovie ->
                    if (favoriteMovie.id == movieDetailsDto.id) {
                        isFavorite = true
                    }
                }
                emit(
                    Resource.Success(
                        MovieDetail(
                            movieDetailsDto.id,
                            movieDetailsDto.title,
                            BASE_IMAGE_URL + movieDetailsDto.backdrop_path,
                            BASE_IMAGE_URL + movieDetailsDto.poster_path,
                            movieDetailsDto.tagline,
                            movieDetailsDto.release_date?.split("-")?.get(0) ?: "",
                            movieDetailsDto.vote_average,
                            movieDetailsDto.vote_count,
                            movieDetailsDto.overview,
                            isFavorite
                        )
                    )
                )
            }

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}