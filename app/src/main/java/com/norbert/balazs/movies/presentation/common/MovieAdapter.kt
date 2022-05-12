package com.norbert.balazs.movies.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.norbert.balazs.movies.R
import com.norbert.balazs.movies.domain.model.Movie
import com.norbert.balazs.movies.presentation.detail.MovieDetailActivity

class MovieAdapter(private val stateChangeObserver: FavoriteStateChangeObserver) :
    RecyclerView.Adapter<MovieVH>() {

    private val movieList = mutableListOf<Movie>()

    fun update(list: List<Movie>) {
        val diffResult = DiffUtil.calculateDiff(
            CategoryDiffUtil(movieList, list)
        )
        movieList.clear()
        movieList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        return MovieVH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_movie,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        val movie = movieList[position]
        holder.bindTo(movie, stateChangeObserver)
        holder.itemView.setOnClickListener {
            holder.itemView.context.startActivity(
                MovieDetailActivity.createIntent(holder.itemView.context, movie.id)
            )
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}