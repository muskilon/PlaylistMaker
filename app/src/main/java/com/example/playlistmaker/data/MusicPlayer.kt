package com.example.playlistmaker.data

import com.example.playlistmaker.databinding.ActivityPlayerBinding

interface MusicPlayer {

    fun preparePlayer(source: String, binding: ActivityPlayerBinding): Int

    fun start()

    fun pause()

    fun getCurrentPosition(): String

}