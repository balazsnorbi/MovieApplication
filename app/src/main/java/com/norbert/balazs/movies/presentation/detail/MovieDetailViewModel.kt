package com.norbert.balazs.movies.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norbert.balazs.movies.domain.model.MovieDetail
import com.norbert.balazs.movies.domain.model.toMovie
import com.norbert.balazs.movies.domain.use_case.GetMovieDetailsUseCase
import com.norbert.balazs.movies.domain.use_case.UpdateFavoriteStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    @Named("GetMovieDetailsUseCase") private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    @Named("UpdateFavoriteStateUseCase") private val updateFavoriteStateUseCase: UpdateFavoriteStateUseCase,
) : ViewModel() {

    suspend fun requestDetails(movieId: Int) = getMovieDetailsUseCase.invoke(movieId)

    fun updateFavoriteStateChange(movieDetail: MovieDetail, newValue: Boolean) {
        viewModelScope.launch {
            updateFavoriteStateUseCase.updateFavoriteState(movieDetail.toMovie(), newValue)
        }
    }
}