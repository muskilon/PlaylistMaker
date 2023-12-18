package com.example.playlistmaker.data

import com.example.playlistmaker.domain.models.PlayerState

interface OnStateChangeListener {
    fun onChange(state: PlayerState)
}