package com.naumenko.rickandmorty.presentation.characters.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.naumenko.rickandmorty.R
import com.naumenko.rickandmorty.presentation.characters.viewmodels.CharactersViewModel
import com.naumenko.rickandmorty.appComponent
import com.naumenko.rickandmorty.base.BaseBottomSheetDialogFragment
import com.naumenko.rickandmorty.base.EMPTY
import com.naumenko.rickandmorty.databinding.FragmentCharacterFilterBottomSheetBinding
import java.util.*

class CharactersFilterBottomSheetFragment :
    BaseBottomSheetDialogFragment<FragmentCharacterFilterBottomSheetBinding, CharactersViewModel>(
        R.layout.fragment_character_filter_bottom_sheet,
        CharactersViewModel::class.java
    ) {

    override fun initDaggerComponent() {
        requireActivity().appComponent.inject(this)
    }

    override fun createBinding(view: View): FragmentCharacterFilterBottomSheetBinding {
        return FragmentCharacterFilterBottomSheetBinding.bind(view)
    }

    private var nameEditText = String.EMPTY
    private var statusTypeChip = String.EMPTY
    private var genderTypeChip = String.EMPTY
    private var speciesTypeChip = String.EMPTY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.charactersStatusTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
            statusTypeChip = if (selectedChipId.isNotEmpty()) {
                val chip = group.findViewById<Chip>(selectedChipId.first())
                val selectedStatusType = chip.text.toString().lowercase(Locale.ROOT)
                selectedStatusType
            } else {
                String.EMPTY
            }
        }
        binding.charactersGenderTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
            genderTypeChip = if (selectedChipId.isNotEmpty()) {
                val chip = group.findViewById<Chip>(selectedChipId.first())
                val selectedGenderType = chip.text.toString().lowercase(Locale.ROOT)
                selectedGenderType
            } else {
                String.EMPTY
            }
        }
        binding.charactersSpeciesTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
            speciesTypeChip = if (selectedChipId.isNotEmpty()) {
                val chip = group.findViewById<Chip>(selectedChipId.first())
                val selectedSpeciesType = chip.text.toString().lowercase(Locale.ROOT)
                selectedSpeciesType
            } else {
                String.EMPTY
            }

        }
        binding.charactersApplyBtn.setOnClickListener {
            nameEditText = binding.charactersNameEdittext.editableText.toString().lowercase().trim()
            if (nameEditText == String.EMPTY && statusTypeChip == String.EMPTY &&
                genderTypeChip == String.EMPTY && speciesTypeChip == String.EMPTY
            ) {
                this.dismiss()
            } else {
                viewModel.saveQuerySearchType(
                    nameEditText,
                    statusTypeChip,
                    genderTypeChip,
                    speciesTypeChip,
                )
                this.dismiss()
            }
        }.toString()
        return binding.root
    }
}