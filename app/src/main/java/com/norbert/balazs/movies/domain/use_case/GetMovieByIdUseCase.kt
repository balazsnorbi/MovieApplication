package com.norbert.balazs.movies.domain.use_case

import com.norbert.balazs.movies.domain.repository.MovieRepository
import javax.inject.Inject
import javax.inject.Named

class GetMovieByIdUseCase @Inject constructor(
    @Named("MovieRepository") private val movieRepository: MovieRepository
) {

}