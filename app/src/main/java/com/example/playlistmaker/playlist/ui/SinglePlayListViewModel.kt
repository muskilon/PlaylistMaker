package com.example.playlistmaker.playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.FilesInteractor
import com.example.playlistmaker.medialibrary.domain.PlayListInteractor
import com.example.playlistmaker.playlist.domain.SinglePlayListState
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.launch

class SinglePlayListViewModel(
    private val filesInteractor: FilesInteractor,
    private val playListInteractor: PlayListInteractor,
    private val tracksInterActor: TracksInteractor
) : ViewModel() {
    private var liveState = MutableLiveData<SinglePlayListState>()
    fun getPlayList(playlistId: Long) {
        viewModelScope.launch {
            var totalTime = 0
            playListInteractor.updateAllPlayListsTracks()
            val currentPlayList = playListInteractor.getSinglePlayList(playlistId)
            val currentPlayListTracks =
                playListInteractor.getTracksForSinglePlayList(currentPlayList.tracks)
            currentPlayListTracks.forEach { track ->
                val units = track.trackTime.split(':')
                val newDuration = (units[0].toInt() * 60) + units[1].toInt()
                totalTime += newDuration
            }
            liveState.postValue(
                SinglePlayListState(
                    currentPlayList = currentPlayList,
                    currentPlayListTracks = currentPlayListTracks,
                    totalTime = totalTime / 60
                )
            )
        }
    }

    fun getState(): LiveData<SinglePlayListState> = liveState
    fun onTrackClick(track: Track) {
        tracksInterActor.setCurrentTrack(track)
    }
}