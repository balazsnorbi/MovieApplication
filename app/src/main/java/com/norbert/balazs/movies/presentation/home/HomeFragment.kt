package com.norbert.balazs.movies.presentation.home

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.norbert.balazs.movies.R
import com.norbert.balazs.movies.common.base.BaseFragment
import com.norbert.balazs.movies.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<MainFragmentBinding>() {

    override fun getLayoutId() = R.layout.main_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPager()
    }

    private fun initPager() {
        activity?.let {
            layoutBinding.pager.adapter = HomePageAdapter(it)
            TabLayoutMediator(layoutBinding.tabLayout, layoutBinding.pager) { tab, position ->
                tab.text = resources.getStringArray(R.array.tabs_title)[position]
            }.attach()
        }
    }
}