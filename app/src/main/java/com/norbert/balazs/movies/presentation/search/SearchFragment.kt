package com.norbert.balazs.movies.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.norbert.balazs.movies.R
import com.norbert.balazs.movies.common.APPLICATION_TAG
import com.norbert.balazs.movies.common.base.BaseFragment
import com.norbert.balazs.movies.databinding.SearchFragmentBinding
import com.norbert.balazs.movies.domain.model.Movie
import com.norbert.balazs.movies.presentation.common.FavoriteStateChangeObserver
import com.norbert.balazs.movies.presentation.common.MovieAdapter
import com.norbert.balazs.movies.presentation.common.SpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchFragmentBinding>() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val viewModel: SearchViewModel by viewModels()

    override fun getLayoutId() = R.layout.search_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutBinding.rvSearchResults.layoutManager = GridLayoutManager(context, 2)
        layoutBinding.rvSearchResults.adapter = MovieAdapter(object : FavoriteStateChangeObserver {
            override fun onFavoriteStateChanged(movie: Movie, newValue: Boolean) {
                viewModel.onFavoriteStateChanged(movie, newValue)
            }
        })
        layoutBinding.rvSearchResults.addItemDecoration(SpaceItemDecoration(8))

        layoutBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i(APPLICATION_TAG, "On query text submit: $query")
                query?.let {
                    viewModel.search(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i(APPLICATION_TAG, "On query text changed: $newText")
                return false
            }
        })

        lifecycleScope.launch {
            viewModel.searchResults.collect {
                it?.let {
                    (layoutBinding.rvSearchResults.adapter as MovieAdapter).update(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        layoutBinding.searchView.requestFocus()
        layoutBinding.searchView.onActionViewExpanded()
    }
}