package com.naumenko.rickandmorty.presentation.locations.models

import com.naumenko.rickandmorty.domain.models.locations.SingleLocation

data class SingleLocationListItem(
    val originSingleLocation: SingleLocation
) {
    val created: String get() = originSingleLocation.created
    val dimension: String get() = originSingleLocation.dimension
    val id: Int get() = originSingleLocation.id
    val name: String get() = originSingleLocation.name
    val type: String get() = originSingleLocation.type
    val url: String get() = originSingleLocation.url
}