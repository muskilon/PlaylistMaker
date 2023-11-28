package com.example.playlistmaker

import android.annotation.SuppressLint
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
enum class VisibilityManager {
    SEARCH,
    SONG_HISTORY,
    NOT_FOUND,
    NO_CONNECTIONS,
    PROGRESS_BAR
}
@SuppressLint("NotifyDataSetChanged")
fun SearchActivity.show(view:VisibilityManager){
    val notFoundPlaceholder = findViewById<FrameLayout>(R.id.not_found_placeholder)
    val noConnectionPlaceholder = findViewById<FrameLayout>(R.id.no_connection_placeholder)
    val searchResultsRecyclerView = findViewById<RecyclerView>(R.id.searchResultsRecyclerView)
    val songHistoryView = findViewById<LinearLayout>(R.id.searchHistory)
    val progressBar = findViewById<ProgressBar>(R.id.progressBar)

    when (view){
        VisibilityManager.SEARCH -> {
            notFoundPlaceholder.visibility = View.GONE
            noConnectionPlaceholder.visibility = View.GONE
            searchResultsRecyclerView.visibility = View.VISIBLE
            songHistoryView.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchResultsAdapter.notifyDataSetChanged()
        }
        VisibilityManager.SONG_HISTORY -> {
            notFoundPlaceholder.visibility = View.GONE
            noConnectionPlaceholder.visibility = View.GONE
            searchResultsRecyclerView.visibility = View.GONE
            songHistoryView.visibility = View.VISIBLE
            songsHistoryAdapter.notifyDataSetChanged()
        }
        VisibilityManager.NOT_FOUND ->{
            notFoundPlaceholder.visibility = View.VISIBLE
            noConnectionPlaceholder.visibility = View.GONE
            searchResultsRecyclerView.visibility = View.GONE
            songHistoryView.visibility = View.GONE
            progressBar.visibility = View.GONE
        }

        VisibilityManager.NO_CONNECTIONS -> {
            notFoundPlaceholder.visibility = View.GONE
            noConnectionPlaceholder.visibility = View.VISIBLE
            searchResultsRecyclerView.visibility = View.GONE
            songHistoryView.visibility = View.GONE
            progressBar.visibility = View.GONE
        }

        VisibilityManager.PROGRESS_BAR -> {
            notFoundPlaceholder.visibility = View.GONE
            noConnectionPlaceholder.visibility = View.GONE
            searchResultsRecyclerView.visibility = View.GONE
            songHistoryView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }
}