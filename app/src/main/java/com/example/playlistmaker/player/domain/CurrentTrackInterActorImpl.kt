package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track


class CurrentTrackInterActorImpl(
    private val repository: CurrentTrackRepository
) : CurrentTrackInterActor {
    override fun getCurrentTrack(): Track {
        return repository.getCurrentTrack()
    }
}