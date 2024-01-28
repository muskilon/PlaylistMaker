package com.example.playlistmaker.player.data

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.MusicPlayerState
import java.util.Locale

class MusicPlayerImpl : MusicPlayer {
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var listener: OnStateChangeListener? = null
    override fun preparePlayer(source: String) {
        mediaPlayer.setDataSource(source)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            listener?.onChange(MusicPlayerState.STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            listener?.onChange(MusicPlayerState.STATE_END_OF_SONG)
        }
    }

    override fun setListener(onStateChangeListener: OnStateChangeListener) {
        listener = onStateChangeListener
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