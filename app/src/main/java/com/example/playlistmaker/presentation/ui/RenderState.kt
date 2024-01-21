package com.example.playlistmaker.presentation.ui

import com.example.playlistmaker.domain.SearchState

interface RenderState {

    fun render(state: SearchState)
    fun showSearch()

    fun showHistory()

    fun showNotFound()

    fun showNetworkError()

    fun showProgressBar()

}