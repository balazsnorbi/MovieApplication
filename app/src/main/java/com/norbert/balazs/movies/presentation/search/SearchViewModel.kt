package com.norbert.balazs.movies.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norbert.balazs.movies.domain.model.Movie
import com.norbert.balazs.movies.domain.use_case.SearchMovieUseCase
import com.norbert.balazs.movies.domain.use_case.UpdateFavoriteStateUseCase
import com.norbert.balazs.movies.common.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SearchViewModel @Inject constructor(
    @Named("SearchMovieUseCase") private val searchMovieUseCase: SearchMovieUseCase,
    @Named("UpdateFavoriteStateUseCase") private val updateFavoriteStateUseCase: UpdateFavoriteStateUseCase,
    @Named("PreferencesManager") private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<Movie>?>(null)
    val searchResults = _searchResults.asStateFlow()

    private var currentSortOrder: String = SORT_RATING_ASC

    init {
        viewModelScope.launch {
            preferencesManager.observe(KEY_SORT_ORDER).collect { sortOrder ->
                sortOrder?.let {
                    currentSortOrder = sortOrder
                    _searchResults.value?.let { list ->
                        sortMovies(list)
                    }
                }
            }
        }
    }

    private fun sortMovies(movies: List<Movie>) {
        when (currentSortOrder) {
            SORT_RATING_ASC -> {
                _searchResults.value = movies.sortedBy {
                    it.averageRating
                }
            }
            SORT_RATING_DESC -> {
                _searchResults.value = movies.sortedByDescending {
                    it.averageRating
                }
            }
            SORT_DATE_ASC -> {
                _searchResults.value = movies.sortedBy {
                    try {
                        it.releaseYear.toInt()
                    } catch (e: Exception) {
                        Calendar.getInstance().get(Calendar.YEAR)
                    }
                }
            }
            SORT_DATE_DESC -> {
                _searchResults.value = movies.sortedByDescending {
                    try {
                        it.releaseYear.toInt()
                    } catch (e: Exception) {
                        Calendar.getInstance().get(Calendar.YEAR)
                    }
                }
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            searchMovieUseCase.invoke(query).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // do nothing here for now
                    }
                    is Resource.Success -> {
                        resource.data?.let {
                            sortMovies(it)
                        }
                    }
                    is Resource.Error -> {
                        // do nothing here for now
                    }
                }
            }
        }
    }

    fun onFavoriteStateChanged(movie: Movie, newValue: Boolean) {
        viewModelScope.launch {
            updateFavoriteStateUseCase.updateFavoriteState(movie, newValue)
        }
    }
}