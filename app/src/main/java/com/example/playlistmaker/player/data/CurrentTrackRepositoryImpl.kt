package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.domain.CurrentTrackRepository
import com.example.playlistmaker.player.domain.TrackModel

class CurrentTrackRepositoryImpl : CurrentTrackRepository {
    override fun getCurrentTrack(): TrackModel {
        return CurrentTrackStorage.getTrackModel()
    }
}