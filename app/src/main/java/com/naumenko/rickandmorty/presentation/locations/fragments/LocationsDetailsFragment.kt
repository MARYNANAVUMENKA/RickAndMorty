package com.naumenko.rickandmorty.presentation.locations.fragments

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
import com.naumenko.rickandmorty.databinding.FragmentLocationDetailsBinding
import com.naumenko.rickandmorty.presentation.HasCustomTitle
import com.naumenko.rickandmorty.presentation.characters.adapter.CharactersListAdapter
import com.naumenko.rickandmorty.presentation.characters.models.SingleCharacterListItem
import com.naumenko.rickandmorty.presentation.locations.viewmodels.LocationsDetailsViewModel
import com.naumenko.rickandmorty.presentation.locations.models.SingleLocationListItem
import com.naumenko.rickandmorty.presentation.navigator
import kotlinx.coroutines.launch

class LocationsDetailsFragment :
    BaseFragment<FragmentLocationDetailsBinding, LocationsDetailsViewModel>(
        R.layout.fragment_location_details,
        LocationsDetailsViewModel::class.java
    ), HasCustomTitle {

    private val adapter by lazy {
        CharactersListAdapter(onClickAction = {
            navigator().onPressedCharacterDetails(it.id)
        })
    }

    override fun initDaggerComponent() {
        requireActivity().appComponent.inject(this)
    }

    override fun createBinding(view: View): FragmentLocationDetailsBinding {
        return FragmentLocationDetailsBinding.bind(view)
    }

    override fun getTitleRes(): Int = R.string.screen_location_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.onFragmentLocationsDetailsCreated(
                requireArguments().getInt(
                    ARG_SINGLE_LOCATION_ID
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkConnectivity()
        binding.locationDetailsRecyclerview.adapter = adapter
        initCollectors()
        refresh()
    }

    private fun refresh() {
        binding.locationDetailsRefresh.setOnRefreshListener {
            viewModel.onFragmentLocationsDetailsCreated(
                requireArguments().getInt(
                    ARG_SINGLE_LOCATION_ID
                )
            )
            initCollectors()
            checkConnectivity()
            binding.locationDetailsRefresh.isRefreshing = false
        }
    }

    private fun checkConnectivity() {
        if (!viewModel.isConnect()) {
            Toast.makeText(
                this@LocationsDetailsFragment.requireActivity(),
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
                showSingleLocation(state.single as SingleLocationListItem)
                if (state.list != null) {
                    showList(state.list as List<SingleCharacterListItem>?)
                }
            }
        }
    }

    private fun showError(message: String?) {
        with(binding) {
            locationDetailsRecyclerview.isVisible = false
            locationDetailsProgressBar.isVisible = false
            locationDetailsLinear.isVisible = false
            locationDetailsErrorMsg.isVisible = true
            locationDetailsErrorMsg.text = message

        }
    }

    private fun showLoading() {
        with(binding) {
            locationDetailsRecyclerview.isVisible = false
            locationDetailsLinear.isVisible = false
            locationDetailsProgressBar.isVisible = true
            locationDetailsErrorMsg.isVisible = false
        }
    }

    private fun showList(listEpisodes: List<SingleCharacterListItem>?) {
        with(binding) {
            locationDetailsProgressBar.isVisible = false
            locationDetailsErrorMsg.isVisible = false
            locationDetailsRecyclerview.isVisible = true
            adapter.submitList(listEpisodes)
            locationDetailsLinear.isVisible = true
        }
    }

    private fun showSingleLocation(singleLocation: SingleLocationListItem) {
        binding.locationDetailsItemLocation.locationsListNameTextview.text = singleLocation.name
        binding.locationDetailsItemLocation.locationsListTypeTextview.text = singleLocation.type
        binding.locationDetailsItemLocation.locationsListDimensionTextview.text =
            singleLocation.dimension
        with(binding) {
            locationDetailsProgressBar.isVisible = false
            locationDetailsLinear.isVisible = true
        }
    }

    companion object {
        private const val ARG_SINGLE_LOCATION_ID = "ARG_SINGLE_LOCATION_ID"

        fun newInstance(locationId: Int): LocationsDetailsFragment {
            val fragment = LocationsDetailsFragment()
            fragment.arguments = bundleOf(ARG_SINGLE_LOCATION_ID to locationId)
            return fragment
        }
    }
}