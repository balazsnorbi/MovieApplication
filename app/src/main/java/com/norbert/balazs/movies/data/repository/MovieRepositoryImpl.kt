package com.norbert.balazs.movies.data.repository

import com.norbert.balazs.movies.common.API_KEY
import com.norbert.balazs.movies.data.remote.MovieDbApi
import com.norbert.balazs.movies.data.remote.dto.MovieDto
import com.norbert.balazs.movies.data.remote.dto.movie_details.MovieDetailsDto
import com.norbert.balazs.movies.domain.repository.MovieRepository
import javax.inject.Inject
import javax.inject.Named

class MovieRepositoryImpl @Inject constructor(
    @Named("MovieDbApi") private val movieDbApi: MovieDbApi
) : MovieRepository {

    override suspend fun getMovieRecommendations(recommendationType: String): List<MovieDto> {
        return movieDbApi.getRecommendations(recommendationType, API_KEY).results
    }

    override suspend fun searchMovie(query: String): List<MovieDto> {
        return movieDbApi.search(API_KEY, query).results
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsDto {
        return movieDbApi.getMovieDetails(movieId, API_KEY)
    }
}