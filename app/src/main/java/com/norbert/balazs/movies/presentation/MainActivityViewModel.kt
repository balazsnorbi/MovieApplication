package com.norbert.balazs.movies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norbert.balazs.movies.common.KEY_SORT_ORDER
import com.norbert.balazs.movies.common.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @Named("PreferencesManager") private val preferencesManager: PreferencesManager
) : ViewModel() {

    fun updateSortOrder(order: String) {
        viewModelScope.launch {
            preferencesManager.update(KEY_SORT_ORDER, order)
        }
    }
}