package com.norbert.balazs.movies.presentation.common

import com.norbert.balazs.movies.domain.model.Movie

interface FavoriteStateChangeObserver {
    fun onFavoriteStateChanged(movie: Movie, newValue: Boolean)
}