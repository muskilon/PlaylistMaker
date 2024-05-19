package com.example.playlistmaker.medialibrary.domain

import com.example.playlistmaker.search.domain.Track

interface PlayListInterActor {
    suspend fun addTrackToPlayList(playList: PlayList, track: Track)
    suspend fun addPlayList(playList: PlayList)
    suspend fun deletePlayList(playList: PlayList)
    fun getPlayLists(): List<PlayList>
    suspend fun updatePlayLists()
    suspend fun updateSinglePlayList(playList: PlayList)
    suspend fun updateAllPlayListsTracks()
    suspend fun deleteTrackFromPlayList(trackId: String, playList: PlayList)
    fun getSinglePlayList(playlistId: Long): PlayList
    fun getTracksForSinglePlayList(trackList: TrackList): List<Track>
}