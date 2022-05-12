package com.norbert.balazs.movies.presentation.home.category.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norbert.balazs.movies.domain.model.Movie
import com.norbert.balazs.movies.domain.use_case.GetRecommendationsUseCase
import com.norbert.balazs.movies.domain.use_case.UpdateFavoriteStateUseCase
import com.norbert.balazs.movies.common.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    @Named("GetRecommendationUseCase") private val getRecommendationsUseCase: GetRecommendationsUseCase,
    @Named("UpdateFavoriteStateUseCase") private val updateFavoriteStateUseCase: UpdateFavoriteStateUseCase,
    @Named("PreferencesManager") private val preferencesManager: PreferencesManager
) : ViewModel() {

    companion object {
        private const val RECOMMENDATION_USE_CASE = "upcoming"
    }

    suspend fun loadDataAsync() =
        getRecommendationsUseCase.invoke(RECOMMENDATION_USE_CASE).combine(
            preferencesManager.observe(KEY_SORT_ORDER)
        ) { resource, sortOrder ->
            when (resource) {
                is Resource.Loading -> {
                    resource
                }
                is Resource.Success -> {
                    when (sortOrder) {
                        SORT_RATING_ASC -> {
                            Resource.Success(
                                resource.data?.sortedBy {
                                    it.averageRating
                                }
                            )
                        }
                        SORT_RATING_DESC -> {
                            Resource.Success(
                                resource.data?.sortedByDescending {
                                    it.averageRating
                                }
                            )
                        }
                        SORT_DATE_ASC -> {
                            Resource.Success(
                                resource.data?.sortedBy {
                                    try {
                                        it.releaseYear.toInt()
                                    } catch (e: Exception) {
                                        Calendar.getInstance().get(Calendar.YEAR)
                                    }
                                }
                            )
                        }
                        SORT_DATE_DESC -> {
                            Resource.Success(
                                resource.data?.sortedByDescending {
                                    try {
                                        it.releaseYear.toInt()
                                    } catch (e: Exception) {
                                        Calendar.getInstance().get(Calendar.YEAR)
                                    }
                                }
                            )
                        }
                        else -> {
                            Resource.Error("Invalid sorting!", resource.data)
                        }
                    }
                }
                is Resource.Error -> {
                    resource
                }
            }
        }

    fun onFavoriteStateChanged(movie: Movie, newValue: Boolean) {
        viewModelScope.launch {
            updateFavoriteStateUseCase.updateFavoriteState(movie, newValue)
        }
    }
}