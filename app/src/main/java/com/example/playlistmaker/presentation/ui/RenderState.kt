package com.example.playlistmaker.presentation.ui

import com.example.playlistmaker.domain.VisibilityState

interface RenderState {

    fun render(state: VisibilityState)
    fun showSearch()

    fun showHistory()

    fun showNotFound()

    fun showNetworkError()

    fun showProgressBar()

}