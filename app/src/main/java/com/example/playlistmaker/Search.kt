package com.example.playlistmaker

import android.annotation.SuppressLint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val songs = ArrayList<Track>()
class SearchResponse(
    val results: ArrayList<Track>,
    val resultCount: Int
)
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