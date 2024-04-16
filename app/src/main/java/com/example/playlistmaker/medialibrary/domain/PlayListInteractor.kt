package com.example.playlistmaker.medialibrary.domain

import com.example.playlistmaker.search.domain.Track

interface PlayListInteractor {
    suspend fun addTrackToPlayList(playList: PlayList, track: Track)

    suspend fun addPlayList(playList: PlayList)

    fun getPlayLists(): List<PlayList>

    suspend fun updatePlayLists()

    suspend fun deletePlayList(playList: PlayList)
    suspend fun deleteTrackFromPlayList(trackId: String, playList: PlayList)
    fun getSinglePlayList(playlistId: Long): PlayList
    suspend fun updateAllPlayListsTracks()
    fun getTracksForSinglePlayList(trackList: TrackList): List<Track>


}