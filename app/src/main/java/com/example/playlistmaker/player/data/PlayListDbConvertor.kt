package com.example.playlistmaker.player.data

import android.net.Uri
import androidx.core.net.toUri
import com.example.playlistmaker.db.PlayListEntity
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.medialibrary.domain.TrackList
import com.google.gson.Gson

class PlayListDbConvertor {
    private val gson = Gson()

    fun map(playlist: PlayList): PlayListEntity {
        return PlayListEntity(
            id = playlist.id,
            title = playlist.title,
            description = checkToNull(playlist.description),
            cover = checkToNull(playlist.cover),
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

    private fun checkToNull(value: Any?): String? {
        when (value) {
            is String? -> {
                return if (value.isNullOrEmpty()) null
                else value
            }

            is Uri? -> {
                return value?.toString()
            }
        }
        return null
    }
}