package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.Resource
import com.example.playlistmaker.search.domain.SearchHistory
import com.example.playlistmaker.search.domain.SearchScreenState
import com.example.playlistmaker.search.domain.SearchState
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TracksInterActor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInterActor: TracksInterActor,
) : ViewModel() {
    private var liveState = MutableLiveData<SearchScreenState>()
    private val liveHistorySongs = MutableLiveData<List<Track>>()
    private val songsHistory = tracksInterActor.getSongsHistory()
    private var lastSearch = EMPTY
    private var searchJob: Job? = null


    fun searchSongs(term: String) {
        liveState.postValue(SearchScreenState.Loading)
        lastSearch = term
        viewModelScope.launch {
            tracksInterActor.searchSongs(term)
                .collect { result -> processResult(result) }
        }
    }

    private fun processResult(foundSongs: Resource<List<Track>>) {
        when (foundSongs) {
            is Resource.ConnectionError -> liveState.postValue(
                SearchScreenState.Error(SearchState.NO_CONNECTIONS)
            )

            is Resource.NotFound -> liveState.postValue(
                SearchScreenState.Error(SearchState.NOT_FOUND)
            )
            is Resource.Data -> {
                    tracksInterActor.setSongsStorage(foundSongs.value)
                    liveState.postValue(
                        SearchScreenState.Content(
                            tracksInterActor.getSongsStorage().toList()
                        )
                    )
            }
        }
    }

    fun searchDebounce(term: String) {

        if (term.isEmpty() || term == lastSearch) searchJob?.cancel()
        else {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
                searchSongs(term)
            }
        }
    }

    fun getState(): LiveData<SearchScreenState> = liveState
    fun getSongsHistory(): LiveData<List<Track>> = liveHistorySongs

    fun getSongsHistoryFromStorage() {
        if (tracksInterActor.readHistory().songsHistorySaved.isNotEmpty()) {
            songsHistory.clear()
            songsHistory.addAll(tracksInterActor.readHistory().songsHistorySaved)
            liveHistorySongs.postValue(songsHistory)
        }
    }

    fun clearHistory() {
        tracksInterActor.clearHistory()
        songsHistory.clear()
        liveHistorySongs.postValue(songsHistory)
        liveState.postValue(SearchScreenState.Content(tracksInterActor.getSongsStorage().toList()))
    }

    fun onTrackClick(track: Track) {
        when {
            songsHistory.isEmpty() -> {
                songsHistory.add(track)
                tracksInterActor.writeHistory(SearchHistory(songsHistory.toList()))
            }

            songsHistory.contains(track) -> {
                with(songsHistory) {
                    remove(track)
                    add(0, track)
                }
                tracksInterActor.writeHistory(SearchHistory(songsHistory.toList()))
            }

            songsHistory.size == HISTORY_SIZE -> {
                with(songsHistory) {
                    removeLast()
                    add(0, track)
                }
                tracksInterActor.writeHistory(SearchHistory(songsHistory.toList()))
            }

            else -> {
                songsHistory.add(0, track)
                tracksInterActor.writeHistory(SearchHistory(songsHistory.toList()))
            }
        }
        liveHistorySongs.postValue(songsHistory)
        tracksInterActor.setCurrentTrack(track)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val HISTORY_SIZE = 10
        private const val EMPTY = ""
    }
}