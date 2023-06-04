package com.naumenko.rickandmorty.presentation.locations.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.naumenko.rickandmorty.*
import com.naumenko.rickandmorty.base.BaseFragment
import com.naumenko.rickandmorty.base.State
import com.naumenko.rickandmorty.databinding.FragmentLocationsBinding
import com.naumenko.rickandmorty.presentation.locations.adapter.LocationsListAdapter
import com.naumenko.rickandmorty.presentation.locations.viewmodels.LocationsViewModel
import com.naumenko.rickandmorty.presentation.locations.models.SingleLocationListItem
import com.naumenko.rickandmorty.presentation.navigator
import kotlinx.coroutines.launch

class LocationsListFragment : BaseFragment<FragmentLocationsBinding, LocationsViewModel>(
    R.layout.fragment_locations,
    LocationsViewModel::class.java
), SearchView.OnQueryTextListener {

    private val adapter by lazy {
        LocationsListAdapter(onClickAction = {
            navigator().onPressedLocationsDetails(it.id)
        })
    }

    private var searchViewText: CharSequence? = null

    override fun initDaggerComponent() {
        requireActivity().appComponent.inject(this)
    }

    override fun createBinding(view: View): FragmentLocationsBinding {
        return FragmentLocationsBinding.bind(view)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_KEY, searchViewText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            searchViewText = savedInstanceState.getCharSequence(SEARCH_KEY)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkConnectivity()
        binding.locationsListRecyclerview.adapter = adapter
        initCollectors()
        setupMenu()
        refresh()
    }

    private fun refresh() {
        binding.refreshListLocations.setOnRefreshListener {
            viewModel.onFragmentLocationsListCreated()
            initCollectors()
            checkConnectivity()
            binding.refreshListLocations.isRefreshing = false
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as SearchView
                searchView.setIconifiedByDefault(false)
                searchView.isSubmitButtonEnabled = false
                searchView.setOnQueryTextListener(this@LocationsListFragment)
                if (searchViewText != null) {
                    searchView.setQuery(searchViewText, true)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_filter -> {
                        navigator().onPressedLocationsFilter()
                        true
                    }
                    R.id.menu_filter_off -> {
                        viewModel.onFragmentLocationsListCreated()
                        initCollectors()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        val text = p0 ?: return false
        viewModel.onSearchToolbarClick(text)
        searchViewText = p0
        return true
    }

    private fun checkConnectivity() {
        if (!viewModel.isConnect()) {
            Toast.makeText(
                this@LocationsListFragment.requireActivity(),
                getString(R.string.no_internet_text),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            viewModel.state
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect(::handle)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun handle(state: State) {
        when (state) {
            is State.Error -> showError(state.message)
            State.Loading -> showLoading()
            is State.Success -> showList(state.list as List<SingleLocationListItem>?)
        }
    }

    private fun showError(message: String?) {
        with(binding) {
            locationsListRecyclerview.isVisible = false
            locationsProgressbar.isVisible = false
            locationsErrorMsg.isVisible = true
            locationsErrorMsg.text = message
        }
    }

    private fun showLoading() {
        with(binding) {
            locationsListRecyclerview.isVisible = true
            locationsProgressbar.isVisible = true
            locationsErrorMsg.isVisible = false
        }
    }

    private fun showList(listLocations: List<SingleLocationListItem>?) {
        with(binding) {
            locationsProgressbar.isVisible = false
            locationsErrorMsg.isVisible = false
            locationsListRecyclerview.isVisible = true
            adapter.submitList(listLocations)
        }
    }

    companion object {
        private const val SEARCH_KEY = "SEARCH_KEY"
    }
}