package com.norbert.balazs.movies.data.remote.dto.search_response

import com.norbert.balazs.movies.data.remote.dto.MovieDto

data class SearchResponseDto(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)