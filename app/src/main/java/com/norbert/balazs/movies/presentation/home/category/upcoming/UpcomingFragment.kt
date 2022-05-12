package com.norbert.balazs.movies.presentation.home.category.upcoming

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.norbert.balazs.movies.R
import com.norbert.balazs.movies.common.Resource
import com.norbert.balazs.movies.common.base.BaseFragment
import com.norbert.balazs.movies.databinding.UpcomingFragmentBinding
import com.norbert.balazs.movies.domain.model.Movie
import com.norbert.balazs.movies.presentation.common.FavoriteStateChangeObserver
import com.norbert.balazs.movies.presentation.common.MovieAdapter
import com.norbert.balazs.movies.presentation.common.SpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpcomingFragment : BaseFragment<UpcomingFragmentBinding>() {

    companion object {
        fun newInstance() = UpcomingFragment()
    }

    private val viewModel: UpcomingViewModel by viewModels()

    override fun getLayoutId() = R.layout.upcoming_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutBinding.rvMovies.layoutManager = GridLayoutManager(context, 2)
        layoutBinding.rvMovies.adapter = MovieAdapter(object : FavoriteStateChangeObserver {
            override fun onFavoriteStateChanged(movie: Movie, newValue: Boolean) {
                viewModel.onFavoriteStateChanged(movie, newValue)
            }
        })
        layoutBinding.rvMovies.addItemDecoration(SpaceItemDecoration(8))

        lifecycleScope.launch {
            viewModel.loadDataAsync().collect { movieList ->
                movieList.let { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // do nothing for now
                        }
                        is Resource.Error -> {
                            Toast.makeText(context, "${resource.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is Resource.Success -> {
                            movieList.data?.let {
                                (layoutBinding.rvMovies.adapter as MovieAdapter).update(movieList.data)
                                layoutBinding.rvMovies.scrollToPosition(0)
                            }
                        }
                    }
                }
            }
        }
    }
}