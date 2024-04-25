package com.example.playlistmaker.settings.domain

interface SharingInterActor {
    fun sharePlaylist(message: String)
    fun shareApp()
    fun openTerms()
    fun openSupport()
}