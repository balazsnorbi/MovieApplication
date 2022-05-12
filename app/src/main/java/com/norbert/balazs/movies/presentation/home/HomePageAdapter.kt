package com.norbert.balazs.movies.presentation.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.norbert.balazs.movies.presentation.home.category.now_playing.NowPlayingFragment
import com.norbert.balazs.movies.presentation.home.category.popular.PopularFragment
import com.norbert.balazs.movies.presentation.home.category.top_rated.TopRatedFragment
import com.norbert.balazs.movies.presentation.home.category.upcoming.UpcomingFragment

class HomePageAdapter(activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    companion object {
        private const val PAGE_COUNT = 4
    }

    override fun getItemCount(): Int = PAGE_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NowPlayingFragment.newInstance()
            1 -> PopularFragment.newInstance()
            2 -> TopRatedFragment.newInstance()
            else -> UpcomingFragment.newInstance()
        }
    }
}