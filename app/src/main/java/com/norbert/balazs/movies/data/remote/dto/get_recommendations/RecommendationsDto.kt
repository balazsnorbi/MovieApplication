package com.norbert.balazs.movies.data.remote.dto.get_recommendations

import com.norbert.balazs.movies.data.remote.dto.MovieDto

data class RecommendationsDto(
    val dates: Dates,
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)