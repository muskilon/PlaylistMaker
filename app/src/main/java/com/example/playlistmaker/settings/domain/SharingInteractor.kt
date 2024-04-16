package com.example.playlistmaker.settings.domain

interface SharingInteractor {
    fun sharePlaylist(message: String)
    fun shareApp()
    fun openTerms()
    fun openSupport()
}