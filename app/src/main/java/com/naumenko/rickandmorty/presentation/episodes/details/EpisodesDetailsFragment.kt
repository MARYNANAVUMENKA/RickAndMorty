package com.naumenko.rickandmorty.presentation.episodes.details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.naumenko.rickandmorty.*
import com.naumenko.rickandmorty.base.BaseFragment
import com.naumenko.rickandmorty.base.StateSingle
import com.naumenko.rickandmorty.databinding.FragmentEpisodeDetailsBinding
import com.naumenko.rickandmorty.presentation.HasCustomTitle
import com.naumenko.rickandmorty.presentation.characters.list.CharactersListAdapter
import com.naumenko.rickandmorty.presentation.characters.models.SingleCharacterListItem
import com.naumenko.rickandmorty.presentation.episodes.models.SingleEpisodeListItem
import com.naumenko.rickandmorty.presentation.navigator
import kotlinx.coroutines.launch

class EpisodesDetailsFragment :
    BaseFragment<FragmentEpisodeDetailsBinding, EpisodesDetailsViewModel>(
        R.layout.fragment_episode_details,
        EpisodesDetailsViewModel::class.java
    ), HasCustomTitle {

    private val adapter by lazy {
        CharactersListAdapter(onClickAction = {
            navigator().onPressedCharacterDetails(it.id)
        })
    }

    override fun initDaggerComponent() {
        requireActivity().appComponent.inject(this)
    }

    override fun createBinding(view: View): FragmentEpisodeDetailsBinding {
        return FragmentEpisodeDetailsBinding.bind(view)
    }

    override fun getTitleRes(): Int = R.string.screen_episodes_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.onFragmentEpisodesDetailsCreated(
                requireArguments().getInt(
                    ARG_SINGLE_EPISODES_ID
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkConnectivity()
        binding.episodeDetailsRecyclerview.adapter = adapter
        initCollectors()
        refresh()
    }

    private fun refresh() {
        binding.episodeDetailsRefresh.setOnRefreshListener {
            viewModel.onFragmentEpisodesDetailsCreated(
                requireArguments().getInt(
                    ARG_SINGLE_EPISODES_ID
                )
            )
            checkConnectivity()
            initCollectors()
            binding.episodeDetailsRefresh.isRefreshing = false
        }
    }

    private fun checkConnectivity() {
        if (!viewModel.isConnect()) {
            Toast.makeText(
                this@EpisodesDetailsFragment.requireActivity(),
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
    private fun handle(state: StateSingle) {
        when (state) {
            is StateSingle.Error -> showError(state.message)
            StateSingle.Loading -> showLoading()
            is StateSingle.Success -> {
                showSingleEpisode(state.single as SingleEpisodeListItem)
                showList(state.list as List<SingleCharacterListItem>?)
            }
        }
    }

    private fun showError(message: String?) {
        with(binding) {
            episodeDetailsRecyclerview.isVisible = false
            episodeDetailsProgressBar.isVisible = false
            episodeDetailsLinear.isVisible = false
            episodeDetailsErrorMsg.isVisible = true
            episodeDetailsErrorMsg.text = message
        }
    }

    private fun showLoading() {
        with(binding) {
            episodeDetailsRecyclerview.isVisible = false
            episodeDetailsLinear.isVisible = false
            episodeDetailsProgressBar.isVisible = true
            episodeDetailsErrorMsg.isVisible = false
        }
    }

    private fun showList(listCharacters: List<SingleCharacterListItem>?) {
        with(binding) {
            episodeDetailsProgressBar.isVisible = false
            episodeDetailsErrorMsg.isVisible = false
            episodeDetailsRecyclerview.isVisible = true
            adapter.submitList(listCharacters)
            episodeDetailsLinear.isVisible = true
        }
    }

    private fun showSingleEpisode(singleEpisode: SingleEpisodeListItem) {
        binding.episodeDetailsItemEpisode.episodesListNameTextview.text = singleEpisode.name
        binding.episodeDetailsItemEpisode.episodesListNumberTextview.text = singleEpisode.episode
        binding.episodeDetailsItemEpisode.episodesListDataTextview.text = singleEpisode.airDate
    }

    companion object {
        private const val ARG_SINGLE_EPISODES_ID = "ARG_SINGLE_EPISODES_ID"

        fun newInstance(episodeId: Int): EpisodesDetailsFragment {
            val fragment = EpisodesDetailsFragment()
            fragment.arguments = bundleOf(ARG_SINGLE_EPISODES_ID to episodeId)
            return fragment
        }
    }
}