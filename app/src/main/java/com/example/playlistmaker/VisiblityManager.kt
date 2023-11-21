package com.example.playlistmaker

import android.annotation.SuppressLint
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView


@SuppressLint("NotifyDataSetChanged")
fun SearchActivity.showSearch() {
    val notFoundPlaceholder = findViewById<FrameLayout>(R.id.not_found_placeholder)
    val noConnectionPlaceholder = findViewById<FrameLayout>(R.id.no_connection_placeholder)
    val searchResultsRecyclerView = findViewById<RecyclerView>(R.id.searchResultsRecyclerView)
    val songHistoryView = findViewById<LinearLayout>(R.id.searchHistory)

    notFoundPlaceholder.visibility = View.GONE
    noConnectionPlaceholder.visibility = View.GONE
    searchResultsRecyclerView.visibility = View.VISIBLE
    songHistoryView.visibility = View.GONE
    songsHistoryAdapter.notifyDataSetChanged()
    searchResultsAdapter.notifyDataSetChanged()
}
@SuppressLint("NotifyDataSetChanged")
fun SearchActivity.showSongHistory() {
    val notFoundPlaceholder = findViewById<FrameLayout>(R.id.not_found_placeholder)
    val noConnectionPlaceholder = findViewById<FrameLayout>(R.id.no_connection_placeholder)
    val searchResultsRecyclerView = findViewById<RecyclerView>(R.id.searchResultsRecyclerView)
    val songHistoryView = findViewById<LinearLayout>(R.id.searchHistory)
    notFoundPlaceholder.visibility = View.GONE
    noConnectionPlaceholder.visibility = View.GONE
    searchResultsRecyclerView.visibility = View.GONE
    songHistoryView.visibility = View.VISIBLE
    songsHistoryAdapter.notifyDataSetChanged()
    searchResultsAdapter.notifyDataSetChanged()
}
fun SearchActivity.showNotFound() {
    val notFoundPlaceholder = findViewById<FrameLayout>(R.id.not_found_placeholder)
    val noConnectionPlaceholder = findViewById<FrameLayout>(R.id.no_connection_placeholder)
    val searchResultsRecyclerView = findViewById<RecyclerView>(R.id.searchResultsRecyclerView)
    val songHistoryView = findViewById<LinearLayout>(R.id.searchHistory)
    notFoundPlaceholder.visibility = View.VISIBLE
    noConnectionPlaceholder.visibility = View.GONE
    searchResultsRecyclerView.visibility = View.GONE
    songHistoryView.visibility = View.GONE
}
fun SearchActivity.showNoConnection() {
    val notFoundPlaceholder = findViewById<FrameLayout>(R.id.not_found_placeholder)
    val noConnectionPlaceholder = findViewById<FrameLayout>(R.id.no_connection_placeholder)
    val searchResultsRecyclerView = findViewById<RecyclerView>(R.id.searchResultsRecyclerView)
    val songHistoryView = findViewById<LinearLayout>(R.id.searchHistory)
    notFoundPlaceholder.visibility = View.GONE
    noConnectionPlaceholder.visibility = View.VISIBLE
    searchResultsRecyclerView.visibility = View.GONE
    songHistoryView.visibility = View.GONE
}