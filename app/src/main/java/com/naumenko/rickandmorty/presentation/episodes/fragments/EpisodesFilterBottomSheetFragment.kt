package com.naumenko.rickandmorty.presentation.episodes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.naumenko.rickandmorty.R
import com.naumenko.rickandmorty.appComponent
import com.naumenko.rickandmorty.base.BaseBottomSheetDialogFragment
import com.naumenko.rickandmorty.base.EMPTY
import com.naumenko.rickandmorty.databinding.FragmentEpisodeFilterBottomSheetBinding
import com.naumenko.rickandmorty.presentation.episodes.viewmodels.EpisodesViewModel
import java.util.*

class EpisodesFilterBottomSheetFragment :
    BaseBottomSheetDialogFragment<FragmentEpisodeFilterBottomSheetBinding, EpisodesViewModel>(
        R.layout.fragment_episode_filter_bottom_sheet,
        EpisodesViewModel::class.java
    ) {
    private var nameEditText = String.EMPTY
    private var numberTypeChip = String.EMPTY

    override fun initDaggerComponent() {
        requireActivity().appComponent.inject(this)
    }

    override fun createBinding(view: View): FragmentEpisodeFilterBottomSheetBinding {
        return FragmentEpisodeFilterBottomSheetBinding.bind(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.episodesNumberChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
            numberTypeChip = if (selectedChipId.isNotEmpty()) {
                val chip = group.findViewById<Chip>(selectedChipId.first())
                val selectedNumberType = chip.text.toString().lowercase(Locale.ROOT)
                selectedNumberType
            } else {
                String.EMPTY
            }
        }
        binding.episodesApplyBtn.setOnClickListener {
            nameEditText = binding.episodesNameEdittext.editableText.toString().lowercase().trim()
            if (nameEditText == String.EMPTY && numberTypeChip == String.EMPTY) {
                this.dismiss()
            } else {
                viewModel.saveQuerySearchEpisodeType(
                    nameEditText,
                    numberTypeChip
                )
                this.dismiss()
            }
        }.toString()
        return binding.root
    }
}