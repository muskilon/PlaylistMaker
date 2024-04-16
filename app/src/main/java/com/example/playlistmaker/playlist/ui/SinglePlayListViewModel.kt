package com.example.playlistmaker.playlist.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.R
import com.example.playlistmaker.medialibrary.domain.FilesInteractor
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.medialibrary.domain.PlayListInteractor
import com.example.playlistmaker.playlist.domain.SinglePlayListState
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.settings.domain.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SinglePlayListViewModel(
    private val sharingInteractor: SharingInteractor,
    private val filesInteractor: FilesInteractor,
    private val playListInteractor: PlayListInteractor,
    private val tracksInterActor: TracksInteractor
) : ViewModel() {
    private var liveState = MutableLiveData<SinglePlayListState>()
    private lateinit var currentPlayList: PlayList
    private lateinit var currentPlayListTracks: List<Track>
    private var totalDuration = 0

    fun getPlayList(playlistId: Long) {
        viewModelScope.launch {
            var totalTime = 0
            playListInteractor.updateAllPlayListsTracks()
            currentPlayList = playListInteractor.getSinglePlayList(playlistId)
            currentPlayListTracks =
                playListInteractor.getTracksForSinglePlayList(currentPlayList.tracks)
            currentPlayListTracks.forEach { track ->
                val units = track.trackTime.split(':')
                val newDuration = (units[0].toInt() * 60) + units[1].toInt()
                totalTime += newDuration
            }
            totalDuration = totalTime / 60
            liveState.postValue(
                SinglePlayListState(
                    currentPlayList = currentPlayList,
                    currentPlayListTracks = currentPlayListTracks,
                    totalTime = totalDuration
                )
            )
        }
    }

    fun deletePlayList() {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.deletePlayList(liveState.value!!.currentPlayList)
            if (liveState.value!!.currentPlayList.cover.toString().isEmpty()) {
                filesInteractor.deletePlayListCover(liveState.value!!.currentPlayList.cover!!)
            }
        }
    }

    fun deleteTrackFromPlayList(trackId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.deleteTrackFromPlayList(trackId, liveState.value!!.currentPlayList)
            getPlayList(liveState.value!!.currentPlayList.id)
        }
    }

    fun sharePlayList() {
        sharingInteractor.sharePlaylist(messageGenerator())
    }

    private fun messageGenerator(): String {
        val trackCount = MyApplication.getAppResources()
            .getQuantityString(
                R.plurals.track_plurals,
                currentPlayList.trackCount,
                currentPlayList.trackCount
            )
        val description = if (!currentPlayList.description.isNullOrEmpty())
            "\n${currentPlayList.description}\n"
        else "\n"
        var message = currentPlayList.title + description + trackCount
        Log.d("TAG", totalDuration.toString())
        currentPlayListTracks.forEachIndexed { index, track ->
            message =
                message + "\n${(index + 1)}" + ". ${track.artistName}" + " - " + track.trackName + " (${track.trackTime})"
        }
        return message
    }

    fun getState(): LiveData<SinglePlayListState> = liveState
    fun onTrackClick(track: Track) {
        tracksInterActor.setCurrentTrack(track)
    }
}