package com.norbert.balazs.movies.presentation.common

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.request.ImageRequest
import coil.target.Target
import com.norbert.balazs.movies.R
import com.norbert.balazs.movies.databinding.ListItemMovieBinding
import com.norbert.balazs.movies.domain.model.Movie

class MovieVH(private val binding: ListItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(movie: Movie, stateChangeObserver: FavoriteStateChangeObserver) {
        Coil.enqueue(
            ImageRequest.Builder(binding.posterImage.context)
                .data(movie.posterUrl)
                .target(object : Target {
                    override fun onSuccess(result: Drawable) {
                        super.onSuccess(result)
                        binding.posterImage.setImageBitmap(result.toBitmap())
                    }
                }).build()
        )

        binding.tvYear.text = movie.releaseYear
        binding.tvRating.text = String.format("%.2f", movie.averageRating)
        binding.ivFavorites.setOnClickListener {
            stateChangeObserver.onFavoriteStateChanged(movie, !movie.isFavorite)
        }
        binding.ivFavorites.setImageDrawable(
            when {
                movie.isFavorite -> {
                    ContextCompat.getDrawable(
                        binding.ivFavorites.context,
                        R.drawable.baseline_favorite_black_18
                    )
                }
                else -> {
                    ContextCompat.getDrawable(
                        binding.ivFavorites.context,
                        R.drawable.baseline_favorite_border_black_18
                    )
                }
            }
        )
    }
}