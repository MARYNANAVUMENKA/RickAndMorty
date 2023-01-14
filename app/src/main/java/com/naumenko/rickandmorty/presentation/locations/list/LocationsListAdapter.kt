package com.naumenko.rickandmorty.presentation.locations.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naumenko.rickandmorty.databinding.ItemLocationBinding
import com.naumenko.rickandmorty.presentation.locations.models.SingleLocationListItem

class LocationsListAdapter(
    private val onClickAction: (SingleLocationListItem) -> Unit,
) : ListAdapter<SingleLocationListItem, LocationsListAdapter.LocationHolder>(LocationsItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(inflater, parent, false)
        return LocationHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location)
        holder.binding.root.setOnClickListener {
            onClickAction(location)
        }
    }

    class LocationHolder(
        val binding: ItemLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: SingleLocationListItem) {
            with(binding) {
                root.tag = location
                locationsListNameTextview.text = location.name
                locationsListTypeTextview.text = location.type
                locationsListDimensionTextview.text = location.dimension

            }
        }
    }
}

object LocationsItemCallback : DiffUtil.ItemCallback<SingleLocationListItem>() {

    override fun areItemsTheSame(
        oldItem: SingleLocationListItem,
        newItem: SingleLocationListItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SingleLocationListItem,
        newItem: SingleLocationListItem
    ): Boolean {
        return oldItem == newItem
    }
}