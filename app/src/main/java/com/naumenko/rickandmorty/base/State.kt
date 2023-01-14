package com.naumenko.rickandmorty.base

sealed class State(
    val list: List<*>? = null,
    val message: String? = null
) {
    class Success(list: List<*>) : State(list)
    class Error(message: String) : State(null, message)
    object Loading : State()
}

sealed class StateSingle(
    val single: Any? = null,
    val list: List<*>? = null,
    val message: String? = null
) {
    class Success(single: Any?, list: List<*>?) : StateSingle(single, list)
    class Error(message: String) : StateSingle(null, null, message)
    object Loading : StateSingle()
}