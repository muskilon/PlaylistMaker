package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.SearchState

interface RenderState {

    fun render(state: SearchState)
    fun showSearch()

    fun showHistory()

    fun showNotFound()

    fun showNetworkError()

    fun showProgressBar()

}