package com.example.playlistmaker.player.data

import com.example.playlistmaker.db.SongsEntity
import com.example.playlistmaker.search.domain.Track

class SongsDbConvertor {

    fun map(track: Track): SongsEntity {
        return SongsEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.artworkUrl512,
            track.previewUrl,
            track.collectionName,
            track.country,
            track.primaryGenreName,
            track.year
        )
    }

    fun map(track: SongsEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.artworkUrl512,
            track.previewUrl,
            track.collectionName,
            track.country,
            track.primaryGenreName,
            track.year
        )
    }
}