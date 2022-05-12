package com.norbert.balazs.movies.presentation.home.category.now_playing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.norbert.balazs.movies.R
import com.norbert.balazs.movies.common.Resource
import com.norbert.balazs.movies.databinding.NowPlayingFragmentBinding
import com.norbert.balazs.movies.domain.model.Movie
import com.norbert.balazs.movies.presentation.common.FavoriteStateChangeObserver
import com.norbert.balazs.movies.presentation.common.MovieAdapter
import com.norbert.balazs.movies.presentation.common.SpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NowPlayingFragment : Fragment() {

    companion object {
        fun newInstance() = NowPlayingFragment()
    }

    private lateinit var layout: NowPlayingFragmentBinding

    private val viewModel: NowPlayingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = DataBindingUtil.inflate(inflater, R.layout.now_playing_fragment, container, false)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout.rvMovies.layoutManager = GridLayoutManager(context, 2)
        layout.rvMovies.adapter = MovieAdapter(object : FavoriteStateChangeObserver {
            override fun onFavoriteStateChanged(movie: Movie, newValue: Boolean) {
                viewModel.onFavoriteStateChanged(movie, newValue)
            }
        })
        layout.rvMovies.addItemDecoration(SpaceItemDecoration(8))

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
                                (layout.rvMovies.adapter as MovieAdapter).update(movieList.data)
                                layout.rvMovies.scrollToPosition(0)
                            }
                        }
                    }
                }
            }
        }
    }
}