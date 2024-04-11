package com.example.playlistmaker.search.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.Resource
import com.example.playlistmaker.search.domain.SearchHistory
import com.example.playlistmaker.search.domain.SearchScreenState
import com.example.playlistmaker.search.domain.SearchState
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
) : ViewModel() {
    private var liveState = MutableLiveData<SearchScreenState>()
    private val liveHistorySongs = MutableLiveData<List<Track>>()
    private val songsHistory = tracksInteractor.getSongsHistory()
    private var searchJob: Job? = null


    fun searchSongs(term: String) {
        liveState.postValue(SearchScreenState.Loading)
        viewModelScope.launch {
            tracksInteractor.searchSongs(term)
                .collect { result -> processResult(result) }
        }
    }

    private fun processResult(foundSongs: Resource<List<Track>>) {
            when (foundSongs) {
                is Resource.ConnectionError -> liveState.postValue(
                    SearchScreenState.Error(
                        SearchState.NO_CONNECTIONS
                    )
                )

                is Resource.NotFound -> liveState.postValue(SearchScreenState.Error(SearchState.NOT_FOUND))
                is Resource.Data -> {
                    if (foundSongs.value.isEmpty()) liveState.postValue(
                        SearchScreenState.Error(
                            SearchState.NOT_FOUND
                        )
                    )
                    else {
                        tracksInteractor.setSongsStorage(foundSongs.value)
                        liveState.postValue(
                            SearchScreenState.Content(
                                tracksInteractor.getSongsStorage().toList()
                            )
                        )
                    }
                }
            }
        }

    fun searchDebounce(term: String) {

        if (term.isEmpty()) searchJob?.cancel()
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
        if (tracksInteractor.readHistory().songsHistorySaved.isNotEmpty()) {
            songsHistory.clear()
            songsHistory.addAll(tracksInteractor.readHistory().songsHistorySaved)
            liveHistorySongs.postValue(songsHistory)
        }
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        songsHistory.clear()
        liveHistorySongs.postValue(songsHistory)
        liveState.postValue(SearchScreenState.Content(tracksInteractor.getSongsStorage().toList()))
    }

    fun onTrackClick(track: Track) {
        Log.d("ON_CLICK", track.trackName)
        when {
            songsHistory.isEmpty() -> {
                songsHistory.add(track)
                tracksInteractor.writeHistory(SearchHistory(songsHistory.toList()))
            }

            songsHistory.contains(track) -> {
                songsHistory.remove(track)
                songsHistory.add(0, track)
                tracksInteractor.writeHistory(SearchHistory(songsHistory.toList()))
            }

            songsHistory.size == HISTORY_SIZE -> {
                songsHistory.removeLast()
                songsHistory.add(0, track)
                tracksInteractor.writeHistory(SearchHistory(songsHistory.toList()))
            }

            else -> {
                songsHistory.add(0, track)
                tracksInteractor.writeHistory(SearchHistory(songsHistory.toList()))
            }
        }
        liveHistorySongs.postValue(songsHistory)
        tracksInteractor.setCurrentTrack(track)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val HISTORY_SIZE = 10
    }
}