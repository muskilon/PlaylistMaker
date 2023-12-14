package com.example.playlistmaker.domain

import android.media.MediaPlayer
import com.example.playlistmaker.PlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.data.MusicPlayer

class MplayerImpl : MusicPlayer {
    private var mediaPlayer = MediaPlayer()
    override fun preparePlayer(source: String) {
        mediaPlayer.setDataSource(source)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isClickable = true
            binding.timeElapsed.text = PlayerActivity.START_TIMER
            playerState = PlayerActivity.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageResource(R.drawable.play_button)
            binding.timeElapsed.text = PlayerActivity.START_TIMER
            playerState = PlayerActivity.STATE_PREPARED
        }
    }

}