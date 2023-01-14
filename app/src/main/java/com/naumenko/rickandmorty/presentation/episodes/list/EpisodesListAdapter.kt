package com.naumenko.rickandmorty.presentation.episodes.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naumenko.rickandmorty.databinding.ItemEpisodesBinding
import com.naumenko.rickandmorty.presentation.episodes.models.SingleEpisodeListItem

class EpisodesListAdapter(
    private val onClickAction: (SingleEpisodeListItem) -> Unit,
) : ListAdapter<SingleEpisodeListItem, EpisodesListAdapter.EpisodeHolder>(EpisodesItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEpisodesBinding.inflate(inflater, parent, false)
        return EpisodeHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeHolder, position: Int) {
        val episode = getItem(position)
        holder.bind(episode)
        holder.binding.root.setOnClickListener {
            onClickAction(episode)
        }
    }

    class EpisodeHolder(
        val binding: ItemEpisodesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: SingleEpisodeListItem) {
            with(binding) {
                root.tag = episode
                episodesListNameTextview.text = episode.name
                episodesListNumberTextview.text = episode.episode
                episodesListDataTextview.text = episode.airDate

            }
        }
    }
}

object EpisodesItemCallback : DiffUtil.ItemCallback<SingleEpisodeListItem>() {

    override fun areItemsTheSame(
        oldItem: SingleEpisodeListItem, newItem: SingleEpisodeListItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SingleEpisodeListItem, newItem: SingleEpisodeListItem
    ): Boolean {
        return oldItem == newItem
    }
}