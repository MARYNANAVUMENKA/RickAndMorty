package com.naumenko.rickandmorty.presentation.characters.details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.naumenko.rickandmorty.*
import com.naumenko.rickandmorty.base.BaseFragment
import com.naumenko.rickandmorty.base.EMPTY
import com.naumenko.rickandmorty.base.StateSingle
import com.naumenko.rickandmorty.databinding.FragmentCharacterDetailsBinding
import com.naumenko.rickandmorty.presentation.HasCustomTitle
import com.naumenko.rickandmorty.presentation.characters.models.SingleCharacterListItem
import com.naumenko.rickandmorty.presentation.episodes.list.EpisodesListAdapter
import com.naumenko.rickandmorty.presentation.episodes.models.SingleEpisodeListItem
import com.naumenko.rickandmorty.presentation.navigator
import kotlinx.coroutines.launch

class CharactersDetailsFragment :
    BaseFragment<FragmentCharacterDetailsBinding, CharactersDetailsViewModel>(
        R.layout.fragment_character_details,
        CharactersDetailsViewModel::class.java
    ), HasCustomTitle {

    private var characterLocationUrl: String = String.EMPTY
    private var characterOriginUrl: String = String.EMPTY

    private val adapter by lazy {
        EpisodesListAdapter(onClickAction = {
            navigator().onPressedEpisodesDetails(it.id)
        })
    }

    override fun initDaggerComponent() {
        requireActivity().appComponent.inject(this)
    }

    override fun createBinding(view: View): FragmentCharacterDetailsBinding {
        return FragmentCharacterDetailsBinding.bind(view)
    }

    override fun getTitleRes(): Int = R.string.screen_character_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.onFragmentCharactersDetailsCreated(
                requireArguments().getInt(ARG_SINGLE_CHARACTER_ID)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkConnectivity()
        binding.charactersDetailsRecyclerview.adapter = adapter
        initCollectors()
        refresh()
        binding.characterDetailsTextviewLocation.setOnClickListener {
            navigator().onPressedLocationsDetails(
                characterLocationUrl.substring(41).toInt()
            )
        }
        binding.characterDetailsTextviewOrigin.setOnClickListener {
            if (characterOriginUrl != String.EMPTY) {
                navigator().onPressedLocationsDetails(
                    characterOriginUrl.substring(41).toInt()
                )
            }
        }
    }

    private fun refresh() {
        binding.refreshCharactersDetails.setOnRefreshListener {
            viewModel.onFragmentCharactersDetailsCreated(
                requireArguments().getInt(ARG_SINGLE_CHARACTER_ID)
            )
            initCollectors()
            checkConnectivity()
            binding.refreshCharactersDetails.isRefreshing = false
        }
    }

    private fun checkConnectivity() {
        if (!viewModel.isConnect()) {
            Toast.makeText(
                this@CharactersDetailsFragment.requireActivity(),
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
                showSingleCharacter(state.single as SingleCharacterListItem)
                showList(state.list as List<SingleEpisodeListItem>?)
            }
        }
    }

    private fun showError(message: String?) {
        with(binding) {
            charactersDetailsRecyclerview.isVisible = false
            charactersDetailsProgressBar.isVisible = false
            characterMaterialCard.isVisible = false
            charactersDetailsErrorMsg.isVisible = true
            charactersDetailsErrorMsg.text = message
        }
    }

    private fun showLoading() {
        with(binding) {
            charactersDetailsRecyclerview.isVisible = false
            characterMaterialCard.isVisible = false
            charactersDetailsProgressBar.isVisible = true
            charactersDetailsErrorMsg.isVisible = false
        }
    }

    private fun showList(listEpisodes: List<SingleEpisodeListItem>?) {
        with(binding) {
            charactersDetailsProgressBar.isVisible = false
            charactersDetailsErrorMsg.isVisible = false
            charactersDetailsRecyclerview.isVisible = true
            adapter.submitList(listEpisodes)
            characterMaterialCard.isVisible = true
        }
    }

    private fun showSingleCharacter(singleCharacter: SingleCharacterListItem) {
        binding.characterDetailsTextviewName.text = singleCharacter.name
        binding.characterDetailsTextviewSpecies.text = singleCharacter.species
        binding.characterDetailsTextviewStatus.text = singleCharacter.status
        binding.characterDetailsTextviewGender.text = singleCharacter.gender
        binding.characterDetailsTextviewLocation.text = singleCharacter.locationName
        binding.characterDetailsTextviewOrigin.text = singleCharacter.originName
        binding.characterDetailsImageview.load(singleCharacter.image) {
            transformations(
                RoundedCornersTransformation(15f)
            )
            placeholder(R.drawable.circle)
            error(R.drawable.circle_placeholder)
        }
        characterLocationUrl = singleCharacter.locationUrl
        characterOriginUrl = singleCharacter.originUrl
    }

    companion object {
        private const val ARG_SINGLE_CHARACTER_ID = "ARG_SINGLE_CHARACTER_ID"

        fun newInstance(characterId: Int): CharactersDetailsFragment {
            val fragment = CharactersDetailsFragment()
            fragment.arguments = bundleOf(ARG_SINGLE_CHARACTER_ID to characterId)
            return fragment
        }
    }
}