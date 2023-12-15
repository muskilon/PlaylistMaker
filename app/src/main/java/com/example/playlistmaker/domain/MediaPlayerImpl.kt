package com.example.playlistmaker.domain

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import com.example.playlistmaker.PlayerActivity
import com.example.playlistmaker.data.MusicPlayer
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import java.util.Locale

class MediaPlayerImpl : MusicPlayer {
    val mediaPlayer = MediaPlayer()
    private var playerState: Int = PlayerActivity.STATE_PREPARED
    override fun preparePlayer(source: String, binding: ActivityPlayerBinding): Int {
        mediaPlayer.setDataSource(source)
        mediaPlayer.prepareAsync()
//        mediaPlayer.setOnPreparedListener {
//            binding.playButton.isClickable = true
//            binding.timeElapsed.text = PlayerActivity.TIMER_ZERO
//            playerState = PlayerActivity.STATE_PREPARED
//        }
//        mediaPlayer.setOnCompletionListener {
//            binding.playButton.setImageResource(R.drawable.play_button)
//            binding.timeElapsed.text = PlayerActivity.TIMER_ZERO
//            playerState = PlayerActivity.STATE_PREPARED
//        }
        return PlayerActivity.STATE_PREPARED
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

}