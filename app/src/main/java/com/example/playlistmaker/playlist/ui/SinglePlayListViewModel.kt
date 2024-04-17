package com.example.playlistmaker.playlist.ui

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

    fun getPlayList(playlistId: Long) {
        viewModelScope.launch {
            var totalTime = 0
            playListInteractor.updatePlayLists()
            playListInteractor.updateAllPlayListsTracks()
            currentPlayList = playListInteractor.getSinglePlayList(playlistId)
            currentPlayListTracks =
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
        val message = buildString {
            this.append(currentPlayList.title)
            if (!currentPlayList.description.isNullOrEmpty()) {
                this.append("\n${currentPlayList.description}\n")
            } else this.append("\n")
            this.append(
                MyApplication.getAppResources().getQuantityString(
                    R.plurals.track_plurals,
                    currentPlayList.trackCount,
                    currentPlayList.trackCount
                )
            )
            currentPlayListTracks.forEachIndexed { index, track ->
                this.append("\n${(index + 1)}")
                this.append(". ${track.artistName} - ")
                this.append(track.trackName)
                this.append(" (${track.trackTime})")
            }
        }
        return message
    }

    fun getState(): LiveData<SinglePlayListState> = liveState
    fun onTrackClick(track: Track) {
        tracksInterActor.setCurrentTrack(track)
    }
}