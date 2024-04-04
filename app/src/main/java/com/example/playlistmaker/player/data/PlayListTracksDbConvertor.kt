package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.data.db.PlayListTrackEntity
import com.example.playlistmaker.search.domain.Track

class PlayListTracksDbConvertor {

    fun map(track: Track, playListId: Long): PlayListTrackEntity {
        return PlayListTrackEntity(
            trackId = track.trackId,
            playListId = playListId,
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

    fun map(track: PlayListTrackEntity): Track {
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