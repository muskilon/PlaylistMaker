package com.example.playlistmaker.playlist.ui

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.R
import com.example.playlistmaker.medialibrary.domain.FilesInterActor
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.medialibrary.domain.PlayListInterActor
import com.example.playlistmaker.playlist.domain.SinglePlayListState
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TracksInterActor
import com.example.playlistmaker.settings.domain.SharingInterActor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class SinglePlayListViewModel(
    private val sharingInterActor: SharingInterActor,
    private val filesInterActor: FilesInterActor,
    private val playListInterActor: PlayListInterActor,
    private val tracksInterActor: TracksInterActor
) : ViewModel() {
    private var liveState = MutableLiveData<SinglePlayListState>()
    private lateinit var currentPlayList: PlayList
    private lateinit var currentPlayListTracks: List<Track>

    @SuppressLint("SimpleDateFormat")
    fun getPlayList(playlistId: Long) {
        viewModelScope.launch {
            playListInterActor.updatePlayLists()
            playListInterActor.updateAllPlayListsTracks()

            currentPlayList = playListInterActor.getSinglePlayList(playlistId)
            currentPlayListTracks =
                playListInterActor.getTracksForSinglePlayList(currentPlayList.tracks)

            var totalTime = 0L
            val format = SimpleDateFormat("mmm:ss,z")
            currentPlayListTracks.forEach { track ->
                totalTime += format.parse(track.trackTime + UTC)?.time ?: 0
            }

            liveState.postValue(
                SinglePlayListState(
                    currentPlayList = currentPlayList,
                    currentPlayListTracks = currentPlayListTracks,
                    totalTime = TimeUnit.MILLISECONDS.toMinutes(totalTime).toInt()
                )
            )
        }
    }

    fun deletePlayList() {
        viewModelScope.launch(Dispatchers.IO) {
            liveState.value?.let { value ->
                playListInterActor.deletePlayList(value.currentPlayList)
                value.currentPlayList.cover?.let { uri ->
                    filesInterActor.deletePlayListCover(uri)
                }
            }
        }
    }

    fun deleteTrackFromPlayList(trackId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            liveState.value?.let { value ->
                playListInterActor.deleteTrackFromPlayList(trackId, value.currentPlayList)
                getPlayList(value.currentPlayList.id)
            }
        }
    }

    fun sharePlayList() {
        sharingInterActor.sharePlaylist(messageGenerator())
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

    companion object {
        private const val UTC = ",UTC"
    }
}