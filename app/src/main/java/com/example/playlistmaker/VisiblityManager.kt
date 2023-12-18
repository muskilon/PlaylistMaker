package com.example.playlistmaker

import android.annotation.SuppressLint
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.VisibilityState
import com.example.playlistmaker.presentation.ui.SearchActivity


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
            notFoundPlaceholder.visibility = View.GONE
            noConnectionPlaceholder.visibility = View.GONE
            searchResultsRecyclerView.visibility = View.VISIBLE
            songHistoryView.visibility = View.GONE
            progressBar.visibility = View.GONE
        }

        VisibilityState.SONG_HISTORY -> {
            songsHistoryAdapter.notifyDataSetChanged()
            notFoundPlaceholder.visibility = View.GONE
            noConnectionPlaceholder.visibility = View.GONE
            searchResultsRecyclerView.visibility = View.GONE
            songHistoryView.visibility = View.VISIBLE
        }

        VisibilityState.NOT_FOUND -> {
            notFoundPlaceholder.visibility = View.VISIBLE
            noConnectionPlaceholder.visibility = View.GONE
            searchResultsRecyclerView.visibility = View.GONE
            songHistoryView.visibility = View.GONE
            progressBar.visibility = View.GONE
        }

        VisibilityState.NO_CONNECTIONS -> {
            notFoundPlaceholder.visibility = View.GONE
            noConnectionPlaceholder.visibility = View.VISIBLE
            searchResultsRecyclerView.visibility = View.GONE
            songHistoryView.visibility = View.GONE
            progressBar.visibility = View.GONE
        }

        VisibilityState.PROGRESS_BAR -> {
            notFoundPlaceholder.visibility = View.GONE
            noConnectionPlaceholder.visibility = View.GONE
            searchResultsRecyclerView.visibility = View.GONE
            songHistoryView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }
}