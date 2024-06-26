package com.example.playlistmaker.player.data

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.MusicPlayerState
import java.util.Locale

class MusicPlayerImpl(
    private val mediaPlayer: MediaPlayer,
) : MusicPlayer {
    private var listener: OnStateChangeListener? = null
    override fun preparePlayer(source: String) {
        with(mediaPlayer) {
            reset()
            setDataSource(source)
            prepareAsync()
            setOnPreparedListener {
                listener?.onChange(MusicPlayerState.STATE_PREPARED)
            }
            setOnCompletionListener {
                listener?.onChange(MusicPlayerState.STATE_END_OF_SONG)
            }
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

    override fun reset() {
        mediaPlayer.reset()
    }

    override fun release() {
        mediaPlayer.release()
    }

}