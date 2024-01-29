package com.example.playlistmaker.player.domain


class CurrentTrackInteractorImpl(
    private val repository: CurrentTrackRepository
) : CurrentTrackInteractor {
    override fun getCurrentTrack(): TrackModel {
        return repository.getCurrentTrack()
    }
}