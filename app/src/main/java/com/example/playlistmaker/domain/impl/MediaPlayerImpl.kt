package com.example.playlistmaker.domain.impl

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import com.example.playlistmaker.data.MusicPlayer
import java.util.Locale

class MediaPlayerImpl : MusicPlayer {
    val mediaPlayer = MediaPlayer()
    override fun preparePlayer(source: String) {
        mediaPlayer.setDataSource(source)
        mediaPlayer.prepareAsync()
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(mediaPlayer.currentPosition).toString()
    }

    override fun stop() {
        mediaPlayer.release()
    }

}