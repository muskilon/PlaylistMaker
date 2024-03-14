package com.example.playlistmaker.search.ui

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.Resource
import com.example.playlistmaker.search.domain.SearchHistory
import com.example.playlistmaker.search.domain.SearchScreenState
import com.example.playlistmaker.search.domain.SearchState
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TracksInteractor

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val handler: Handler
) : ViewModel() {
    private var liveState = MutableLiveData<SearchScreenState>()
    private var searchInput = EMPTY
    private val songs = ArrayList<Track>()
    private val liveHistorySongs = MutableLiveData<List<Track>>()
    private val tempSongs = mutableListOf<Track>()

    private val consumer = object : TracksInteractor.TracksConsumer {
        override fun consume(foundSongs: Resource<List<Track>>) {
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
                        songs.clear()
                        songs.addAll(foundSongs.value)
                        liveState.postValue(SearchScreenState.Content(songs.toList()))
                    }
                }
            }
        }
    }
    private val searchRunnable = Runnable {
        if (searchInput.isNotEmpty()) {
            searchSongs("song", searchInput, "ru")
        }
    }

    fun searchSongs(entity: String, term: String, lang: String) {
        liveState.postValue(SearchScreenState.Loading)
        handler.removeCallbacks(searchRunnable)
        tracksInteractor.searchSongs(entity, term, lang, consumer)
    }

    fun searchDebounce(term: String) {
        searchInput = term
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun getState(): LiveData<SearchScreenState> = liveState
    fun getSongsHistory(): LiveData<List<Track>> = liveHistorySongs

    fun getSongsHistoryFromStorage() {
        if (tracksInteractor.readHistory().songsHistorySaved.isNotEmpty()) {
            tempSongs.clear()
            tempSongs.addAll(tracksInteractor.readHistory().songsHistorySaved)
            liveHistorySongs.postValue(tempSongs)
        }
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        tempSongs.clear()
        liveHistorySongs.postValue(tempSongs)
        liveState.postValue(SearchScreenState.Content(songs.toList()))
    }

    fun onTrackClick(track: Track) {
        when {
            tempSongs.isEmpty() -> {
                tempSongs.add(track)
                tracksInteractor.writeHistory(SearchHistory(tempSongs.toList()))
            }

            tempSongs.contains(track) -> {
                tempSongs.remove(track)
                tempSongs.add(0, track)
                tracksInteractor.writeHistory(SearchHistory(tempSongs.toList()))
            }

            tempSongs.size == HISTORY_SIZE -> {
                tempSongs.removeLast()
                tempSongs.add(0, track)
                tracksInteractor.writeHistory(SearchHistory(tempSongs.toList()))
            }

            else -> {
                tempSongs.add(0, track)
                tracksInteractor.writeHistory(SearchHistory(tempSongs.toList()))
            }
        }
        liveHistorySongs.postValue(tempSongs)
        tracksInteractor.setCurrentTrack(track)
    }

    companion object {
        private const val EMPTY = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val HISTORY_SIZE = 10
    }
}