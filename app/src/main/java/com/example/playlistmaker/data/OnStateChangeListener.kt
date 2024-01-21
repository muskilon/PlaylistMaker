package com.example.playlistmaker.data

import com.example.playlistmaker.domain.MusicPlayerState

interface OnStateChangeListener {
    fun onChange(state: MusicPlayerState)
}