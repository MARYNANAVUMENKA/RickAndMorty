package com.naumenko.rickandmorty.presentation.episodes.fragments

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
import com.naumenko.rickandmorty.R
import com.naumenko.rickandmorty.base.State
import com.naumenko.rickandmorty.appComponent
import com.naumenko.rickandmorty.base.BaseFragment
import com.naumenko.rickandmorty.databinding.FragmentEpisodesBinding
import com.naumenko.rickandmorty.presentation.episodes.adapter.EpisodesListAdapter
import com.naumenko.rickandmorty.presentation.navigator
import com.naumenko.rickandmorty.presentation.episodes.models.SingleEpisodeListItem
import com.naumenko.rickandmorty.presentation.episodes.viewmodels.EpisodesViewModel
import kotlinx.coroutines.launch

class EpisodesListFragment : BaseFragment<FragmentEpisodesBinding, EpisodesViewModel>(
    R.layout.fragment_episodes,
    EpisodesViewModel::class.java
), SearchView.OnQueryTextListener {


    private val adapter by lazy {
        EpisodesListAdapter(onClickAction = {
            navigator().onPressedEpisodesDetails(it.id)
        })
    }
    private var searchViewText: CharSequence? = null

    override fun initDaggerComponent() {
        requireActivity().appComponent.inject(this)
    }

    override fun createBinding(view: View): FragmentEpisodesBinding {
        return FragmentEpisodesBinding.bind(view)
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
        binding.episodesListRecyclerview.adapter = adapter
        initCollectors()
        setupMenu()
        refresh()
    }

    private fun refresh() {
        binding.refreshListEpisodes.setOnRefreshListener {
            viewModel.onFragmentEpisodesListCreated()
            initCollectors()
            checkConnectivity()
            binding.refreshListEpisodes.isRefreshing = false
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
                searchView.setOnQueryTextListener(this@EpisodesListFragment)
                if (searchViewText != null) {
                    searchView.setQuery(searchViewText, true)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_filter -> {
                        navigator().onPressedEpisodesFilter()
                        true
                    }
                    R.id.menu_filter_off -> {
                        viewModel.onFragmentEpisodesListCreated()
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
                this@EpisodesListFragment.requireActivity(),
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
            is State.Success -> showList(state.list as List<SingleEpisodeListItem>?)
        }
    }

    private fun showError(message: String?) {
        with(binding) {
            episodesListRecyclerview.isVisible = false
            episodesProgressbar.isVisible = false
            episodesErrorMsg.isVisible = true
            episodesErrorMsg.text = message

        }
    }

    private fun showLoading() {
        with(binding) {
            episodesListRecyclerview.isVisible = true
            episodesProgressbar.isVisible = true
            episodesErrorMsg.isVisible = false
        }
    }

    private fun showList(listEpisodes: List<SingleEpisodeListItem>?) {
        with(binding) {
            episodesProgressbar.isVisible = false
            episodesErrorMsg.isVisible = false
            episodesListRecyclerview.isVisible = true
            adapter.submitList(listEpisodes)
        }
    }

    companion object {
        private const val SEARCH_KEY = "SEARCH_KEY"
    }
}