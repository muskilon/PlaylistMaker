package com.example.playlistmaker.player.data

import com.example.playlistmaker.db.SongsEntity
import com.example.playlistmaker.search.domain.Track

class SongsDbConvertor {

    fun map(track: Track): SongsEntity {
        return SongsEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            artworkUrl512 = track.artworkUrl512,
            previewUrl = track.previewUrl,
            collectionName = track.collectionName,
            country = track.country,
            primaryGenreName = track.primaryGenreName,
            year = track.year
        )
    }

    fun map(track: SongsEntity): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            artworkUrl512 = track.artworkUrl512,
            previewUrl = track.previewUrl,
            collectionName = track.collectionName,
            country = track.country,
            primaryGenreName = track.primaryGenreName,
            year = track.year
        )
    }
}