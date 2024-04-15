package com.example.playlistmaker.player.data

import androidx.core.net.toUri
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.medialibrary.domain.TrackList
import com.example.playlistmaker.player.data.db.PlayListEntity
import com.google.gson.Gson

class PlayListDbConvertor {
    private val gson = Gson()

    fun map(playlist: PlayList): PlayListEntity {
        return PlayListEntity(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            cover = playlist.cover.toString(),
            trackCount = playlist.trackCount,
            tracks = gson.toJson(playlist.tracks)
        )
    }

    fun map(playlist: PlayListEntity): PlayList {
        return PlayList(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            cover = playlist.cover?.toUri(),
            trackCount = playlist.trackCount,
            tracks = gson.fromJson(playlist.tracks, TrackList::class.java)
        )
    }
}