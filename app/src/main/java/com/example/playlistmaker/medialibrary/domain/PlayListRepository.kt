package com.example.playlistmaker.medialibrary.domain

import com.example.playlistmaker.search.domain.Track

interface PlayListRepository {
    suspend fun addTrackToPlayList(playList: PlayList, track: Track)

    suspend fun addPlayList(playList: PlayList)

    suspend fun updatePlayListsList()
    suspend fun updateSinglePlayList(playList: PlayList)
    suspend fun updateAllPlayListsTracks()

    fun getPlayLists(): List<PlayList>
    fun getTracksForSinglePlayList(trackList: TrackList): List<Track>
    suspend fun deletePlayList(playList: PlayList)
    suspend fun deleteTrackFromPlayList(trackId: String, playList: PlayList)
    fun getSinglePlayList(playlistId: Long): PlayList

}