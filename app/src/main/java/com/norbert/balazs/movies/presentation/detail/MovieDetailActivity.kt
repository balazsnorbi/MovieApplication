package com.norbert.balazs.movies.presentation.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import coil.load
import com.norbert.balazs.movies.R
import com.norbert.balazs.movies.common.APPLICATION_TAG
import com.norbert.balazs.movies.common.Resource
import com.norbert.balazs.movies.databinding.MovieDetailActivityBinding
import com.norbert.balazs.movies.domain.model.MovieDetail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"

        fun createIntent(context: Context, movieId: Int) =
            Intent(context, MovieDetailActivity::class.java)
                .putExtra(EXTRA_MOVIE_ID, movieId)
    }

    private val viewModel: MovieDetailViewModel by viewModels()

    private lateinit var layout: MovieDetailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = DataBindingUtil.setContentView(this, R.layout.movie_detail_activity)

        val id = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        if (id > 0) {
            lifecycleScope.launch {
                viewModel.requestDetails(id).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            Log.i(APPLICATION_TAG, "Loading movie details for id: $id")
                        }
                        is Resource.Success -> {
                            resource.data?.let {
                                bindTo(it)
                            }
                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                this@MovieDetailActivity,
                                "Loading failed!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        layout.ibClose.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindTo(movieDetail: MovieDetail) {
        layout.ivBackdrop.load(movieDetail.backdropUrl)
        layout.ivPoster.load(movieDetail.posterUrl)
        layout.tvMovieTitle.text = movieDetail.title
        layout.tvMovieDescription.text = movieDetail.tagLine
        layout.tvReleaseYear.text = "Releasedin ${movieDetail.releaseYear}"
        layout.tvRatingAndRatingCount.text =
            String.format("%.2f (${movieDetail.voteCount})", movieDetail.averageRating)
        layout.ivFavorites.setImageDrawable(
            when {
                movieDetail.isFavorite -> {
                    ContextCompat.getDrawable(
                        layout.ivFavorites.context,
                        R.drawable.baseline_favorite_black_18
                    )
                }
                else -> {
                    ContextCompat.getDrawable(
                        layout.ivFavorites.context,
                        R.drawable.baseline_favorite_border_black_18
                    )
                }
            }
        )
        layout.ivFavorites.setOnClickListener {
            viewModel.updateFavoriteStateChange(movieDetail, !movieDetail.isFavorite)
        }
        layout.tvOverviewContent.text = movieDetail.overview
    }
}