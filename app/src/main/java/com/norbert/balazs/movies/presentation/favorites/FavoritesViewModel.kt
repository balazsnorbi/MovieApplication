package com.norbert.balazs.movies.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norbert.balazs.movies.domain.model.Movie
import com.norbert.balazs.movies.domain.use_case.GetFavoritesMoviesUseCase
import com.norbert.balazs.movies.domain.use_case.UpdateFavoriteStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    @Named("UpdateFavoriteStateUseCase") private val updateFavoriteStateUseCase: UpdateFavoriteStateUseCase,
    @Named("GetFavoritesMoviesUseCase") private val getFavoritesMoviesUseCase: GetFavoritesMoviesUseCase
) : ViewModel() {

    suspend fun getFavorites() = getFavoritesMoviesUseCase.invoke()

    fun onFavoriteStateChanged(movie: Movie, newValue: Boolean) {
        viewModelScope.launch {
            updateFavoriteStateUseCase.updateFavoriteState(movie, newValue)
        }
    }
}