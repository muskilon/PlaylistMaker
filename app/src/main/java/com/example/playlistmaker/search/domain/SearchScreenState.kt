package com.example.playlistmaker.search.domain

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    data class Content(
        val songs: List<Track>
    ) : SearchScreenState()

    data class Error(
        val error: SearchState
    ) : SearchScreenState()
}