package com.example.playlistmaker.data


interface MusicPlayer {
    fun preparePlayer(source: String)

    fun start()

    fun pause()

    fun getCurrentPosition(): String

    fun stop()

    fun setListener(onStateChangeListener: OnStateChangeListener)
}