package com.naumenko.rickandmorty.presentation.locations.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.naumenko.rickandmorty.R
import com.naumenko.rickandmorty.appComponent
import com.naumenko.rickandmorty.base.BaseBottomSheetDialogFragment
import com.naumenko.rickandmorty.base.EMPTY
import com.naumenko.rickandmorty.databinding.FragmentLocationFilterBottomSheetBinding
import com.naumenko.rickandmorty.presentation.locations.list.LocationsViewModel
import java.util.*

class LocationsFilterBottomSheetFragment :
    BaseBottomSheetDialogFragment<FragmentLocationFilterBottomSheetBinding, LocationsViewModel>(
        R.layout.fragment_location_filter_bottom_sheet,
        LocationsViewModel::class.java
    ) {
    private var nameEditText = String.EMPTY
    private var locationTypeChip = String.EMPTY
    private var dimensionTypeChip = String.EMPTY


    override fun initDaggerComponent() {
        requireActivity().appComponent.inject(this)
    }

    override fun createBinding(view: View): FragmentLocationFilterBottomSheetBinding {
        return FragmentLocationFilterBottomSheetBinding.bind(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.locationsTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
            locationTypeChip = if (selectedChipId.isNotEmpty()) {
                val chip = group.findViewById<Chip>(selectedChipId.first())
                val selectedLocationType = chip.text.toString().lowercase(Locale.ROOT)
                selectedLocationType
            } else {
                String.EMPTY
            }
        }
        binding.locationsDimensionTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
            dimensionTypeChip = if (selectedChipId.isNotEmpty()) {
                val chip = group.findViewById<Chip>(selectedChipId.first())
                val selectedDimensionType = chip.text.toString().lowercase(Locale.ROOT)
                selectedDimensionType
            } else {
                String.EMPTY
            }
        }

        binding.locationsApplyBtn.setOnClickListener {
            nameEditText = binding.locationsNameEdittext.editableText.toString().lowercase().trim()
            if (nameEditText == String.EMPTY && locationTypeChip == String.EMPTY &&
                dimensionTypeChip == String.EMPTY
            ) {
                this.dismiss()
            } else {
                viewModel.saveQuerySearchLocationType(
                    nameEditText,
                    locationTypeChip,
                    dimensionTypeChip,
                )
                this.dismiss()
            }
        }.toString()
        return binding.root
    }
}