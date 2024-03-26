package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track


class CurrentTrackInteractorImpl(
    private val repository: CurrentTrackRepository
) : CurrentTrackInteractor {
    override fun getCurrentTrack(): Track {
        return repository.getCurrentTrack()
    }
}