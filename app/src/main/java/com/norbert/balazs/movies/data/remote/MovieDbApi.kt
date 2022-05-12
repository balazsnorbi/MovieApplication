package com.norbert.balazs.movies.data.remote

import com.norbert.balazs.movies.data.remote.dto.get_recommendations.RecommendationsDto
import com.norbert.balazs.movies.data.remote.dto.movie_details.MovieDetailsDto
import com.norbert.balazs.movies.data.remote.dto.search_response.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbApi {

    @GET("movie/{recommendation_type}")
    suspend fun getRecommendations(
        @Path(value = "recommendation_type") recommendationType: String,
        @Query("api_key") apiKey: String
    ): RecommendationsDto

    @GET("search/movie")
    suspend fun search(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): SearchResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path(value = "movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDetailsDto
}