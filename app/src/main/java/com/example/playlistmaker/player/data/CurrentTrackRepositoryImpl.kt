package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.domain.CurrentTrackRepository
import com.example.playlistmaker.search.domain.Track

class CurrentTrackRepositoryImpl : CurrentTrackRepository {
    override fun getCurrentTrack(): Track {
        return CurrentTrackStorage.getTrackModel()
    }
}