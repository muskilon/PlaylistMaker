package com.example.playlistmaker

import android.annotation.SuppressLint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun SearchActivity.search(queryInput: String) {
    itunesService.getSearch(queryInput).enqueue(object : Callback<SearchResponse> {
        @SuppressLint("NotifyDataSetChanged")
        override fun onResponse(
            call: Call<SearchResponse>, response: Response<SearchResponse>
        ) {
            if (response.body()?.resultCount == 0) {
                showNotFound()
            } else {
                showSearch()
                songs.clear()
                songs.addAll(response.body()?.results!!)
                searchResultsAdapter.notifyDataSetChanged()
            }
        }

        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
            showNoConnection()
        }

    })
}