package com.norbert.balazs.movies.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.norbert.balazs.movies.R
import com.norbert.balazs.movies.common.SORT_DATE_ASC
import com.norbert.balazs.movies.common.SORT_DATE_DESC
import com.norbert.balazs.movies.common.SORT_RATING_ASC
import com.norbert.balazs.movies.common.SORT_RATING_DESC
import com.norbert.balazs.movies.common.base.BaseActivity
import com.norbert.balazs.movies.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainActivityBinding>() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        configureNavigation()

        initBottomNavigationToolbar()

        viewModel.updateSortOrder(SORT_RATING_ASC)
    }

    override fun getLayoutId() = R.layout.main_activity

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_rating_asc -> {
                viewModel.updateSortOrder(SORT_RATING_ASC)
                true
            }
            R.id.menu_item_rating_desc -> {
                viewModel.updateSortOrder(SORT_RATING_DESC)
                true
            }
            R.id.menu_item_release_date_asc -> {
                viewModel.updateSortOrder(SORT_DATE_ASC)
                true
            }
            R.id.menu_item_release_date_desc -> {
                viewModel.updateSortOrder(SORT_DATE_DESC)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun initBottomNavigationToolbar() {
        val navController = Navigation.findNavController(this, R.id.fragmentContainerView)
        NavigationUI.setupWithNavController(layoutBinding.bottomNavigationView, navController)
    }

    private fun initToolbar() {
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.home)
        setSupportActionBar(toolbar)
        toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.baseline_sort_black_24)
    }

    private fun configureNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.searchFragment,
                R.id.favoritesFragment
            )
        )
        layoutBinding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}