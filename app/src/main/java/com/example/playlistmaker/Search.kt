package com.example.playlistmaker

import android.annotation.SuppressLint
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun SearchActivity.search(queryInput: String) {
    itunesService.getSearch(queryInput).enqueue(object : Callback<SearchResponse> {
        @SuppressLint("NotifyDataSetChanged")
        override fun onResponse(
            call: Call<SearchResponse>, response: Response<SearchResponse>
        ) {
            val notFoundPlaceholder = findViewById<FrameLayout>(R.id.not_found_placeholder)
            val searchResultsRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            if (response.body()?.resultCount == 0) {
                searchResultsRecyclerView.visibility = View.GONE
                notFoundPlaceholder.visibility = View.VISIBLE
            } else {
                searchResultsRecyclerView.visibility = View.VISIBLE
                songs.clear()
                songs.addAll(response.body()?.results!!)
                searchResultsAdapter.notifyDataSetChanged()
            }
        }

        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
            val noConnectionPlaceholder =
                findViewById<FrameLayout>(R.id.no_connection_placeholder)
            val searchResultsRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            searchResultsRecyclerView.visibility = View.GONE
            noConnectionPlaceholder.visibility = View.VISIBLE
        }

    })
}