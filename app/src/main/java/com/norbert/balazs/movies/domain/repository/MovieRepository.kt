package com.norbert.balazs.movies.domain.repository

import com.norbert.balazs.movies.data.remote.dto.MovieDto
import com.norbert.balazs.movies.data.remote.dto.movie_details.MovieDetailsDto

interface MovieRepository {

    suspend fun getMovieRecommendations(recommendationType: String): List<MovieDto>

    suspend fun searchMovie(query: String): List<MovieDto>

    suspend fun getMovieDetails(movieId: Int): MovieDetailsDto
}