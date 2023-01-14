package com.naumenko.rickandmorty.presentation.characters.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.naumenko.rickandmorty.R
import com.naumenko.rickandmorty.databinding.ItemCharacterBinding
import com.naumenko.rickandmorty.presentation.characters.models.SingleCharacterListItem

class CharactersListAdapter(
    private val onClickAction: (SingleCharacterListItem) -> Unit,
) : ListAdapter<SingleCharacterListItem, CharactersListAdapter.CharacterHolder>(
    CharacterItemCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCharacterBinding.inflate(inflater, parent, false)
        return CharacterHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
        holder.binding.root.setOnClickListener {
            onClickAction(character)
        }
    }

    class CharacterHolder(
        val binding: ItemCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: SingleCharacterListItem) {
            with(binding) {
                root.tag = character
                characterNameTextview.text = character.name
                characterSpeciesTextview.text = character.species
                characterGenderTextview.text = character.gender
                characterStatusTextview.text = character.status
                characterImageview.load(character.image) {
                    transformations(RoundedCornersTransformation(15f))
                    placeholder(R.drawable.circle)
                    error(R.drawable.circle_placeholder)
                }
            }
        }
    }
}

object CharacterItemCallback : DiffUtil.ItemCallback<SingleCharacterListItem>() {

    override fun areItemsTheSame(
        oldItem: SingleCharacterListItem,
        newItem: SingleCharacterListItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SingleCharacterListItem,
        newItem: SingleCharacterListItem
    ): Boolean {
        return oldItem == newItem
    }
}