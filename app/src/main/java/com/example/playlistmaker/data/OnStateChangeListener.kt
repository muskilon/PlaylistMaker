package com.example.playlistmaker.data

import com.example.playlistmaker.domain.PlayerState

interface OnStateChangeListener {
    fun onChange(state: PlayerState)
}