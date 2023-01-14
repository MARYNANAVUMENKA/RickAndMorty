package com.naumenko.rickandmorty.presentation.characters.list

import android.os.Bundle
import android.view.*
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
import com.naumenko.rickandmorty.databinding.FragmentCharactersBinding
import com.naumenko.rickandmorty.presentation.characters.models.SingleCharacterListItem
import com.naumenko.rickandmorty.presentation.navigator
import kotlinx.coroutines.launch

class CharactersListFragment : BaseFragment<FragmentCharactersBinding, CharactersViewModel>(
    R.layout.fragment_characters,
    CharactersViewModel::class.java
), SearchView.OnQueryTextListener {

    private val adapter by lazy {
        CharactersListAdapter(onClickAction = {
            navigator().onPressedCharacterDetails(
                it.id
            )
        })
    }
    private var searchViewText: CharSequence? = null

    override fun initDaggerComponent() {
        requireActivity().appComponent.inject(this)
    }

    override fun createBinding(view: View): FragmentCharactersBinding {
        return FragmentCharactersBinding.bind(view)
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
        binding.charactersListRecyclerview.adapter = adapter
        checkConnectivity()
        initCollectors()
        setupMenu()
        refresh()
    }

    private fun refresh() {
        binding.refreshListCharacters.setOnRefreshListener {
            viewModel.onFragmentCharactersListCreated()
            checkConnectivity()
            initCollectors()
            binding.refreshListCharacters.isRefreshing = false
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
                searchView.setOnQueryTextListener(this@CharactersListFragment)
                if (searchViewText != null) {
                    searchView.setQuery(searchViewText, true)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_filter -> {
                        navigator().onPressedCharactersFilter()
                        true
                    }
                    R.id.menu_filter_off -> {
                        viewModel.onFragmentCharactersListCreated()
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
                this@CharactersListFragment.requireActivity(),
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
            is State.Success -> showList(state.list as List<SingleCharacterListItem>?)
        }
    }

    private fun showError(message: String?) {
        with(binding) {
            charactersListRecyclerview.isVisible = false
            charactersProgressBar.isVisible = false
            charactersErrorMsg.isVisible = true
            charactersErrorMsg.text = message

        }
    }

    private fun showLoading() {
        with(binding) {
            charactersListRecyclerview.isVisible = true
            charactersProgressBar.isVisible = true
            charactersErrorMsg.isVisible = false
        }
    }

    private fun showList(listCharacters: List<SingleCharacterListItem>?) {
        with(binding) {
            charactersProgressBar.isVisible = false
            charactersErrorMsg.isVisible = false
            charactersListRecyclerview.isVisible = true
            adapter.submitList(listCharacters)
        }
    }

    companion object {
        private const val SEARCH_KEY = "SEARCH_KEY"
    }
}