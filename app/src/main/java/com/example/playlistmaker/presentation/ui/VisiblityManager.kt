package com.example.playlistmaker.presentation.ui

import android.annotation.SuppressLint
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.VisibilityState


@SuppressLint("NotifyDataSetChanged")
fun SearchActivity.show(view: VisibilityState) {
    val notFoundPlaceholder = findViewById<FrameLayout>(R.id.not_found_placeholder)
    val noConnectionPlaceholder = findViewById<FrameLayout>(R.id.no_connection_placeholder)
    val searchResultsRecyclerView = findViewById<RecyclerView>(R.id.searchResultsRecyclerView)
    val songHistoryView = findViewById<LinearLayout>(R.id.searchHistory)
    val progressBar = findViewById<ProgressBar>(R.id.progressBar)

    when (view) {
        VisibilityState.SEARCH -> {
            searchResultsAdapter.notifyDataSetChanged()
            notFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = false
            searchResultsRecyclerView.isVisible = true
            songHistoryView.isVisible = false
            progressBar.isVisible = false
        }

        VisibilityState.SONG_HISTORY -> {
            songsHistoryAdapter.notifyDataSetChanged()
            notFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = false
            searchResultsRecyclerView.isVisible = false
            songHistoryView.isVisible = true
        }

        VisibilityState.NOT_FOUND -> {
            notFoundPlaceholder.isVisible = true
            noConnectionPlaceholder.isVisible = false
            searchResultsRecyclerView.isVisible = false
            songHistoryView.isVisible = false
            progressBar.isVisible = false
        }

        VisibilityState.NO_CONNECTIONS -> {
            notFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = true
            searchResultsRecyclerView.isVisible = false
            songHistoryView.isVisible = false
            progressBar.isVisible = false
        }

        VisibilityState.PROGRESS_BAR -> {
            notFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = false
            searchResultsRecyclerView.isVisible = false
            songHistoryView.isVisible = false
            progressBar.isVisible = true
        }
    }
}