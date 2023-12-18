package com.example.playlistmaker

import android.annotation.SuppressLint
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.VisibilityState
import com.example.playlistmaker.presentation.ui.SearchActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResponse(
    val results: ArrayList<Track>,
    val resultCount: Int
)
object Search {
    val songs = ArrayList<Track>()
    fun SearchActivity.search(queryInput: String) {
        show(VisibilityState.PROGRESS_BAR)
        itunesService.getSearch("song", queryInput, "ru")
            .enqueue(object : Callback<SearchResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<SearchResponse>, response: Response<SearchResponse>
                ) {
                    if (response.body()?.resultCount == 0 || response.body() == null) {
                        show(VisibilityState.NOT_FOUND)
                    } else {
                        show(VisibilityState.SEARCH)
                        songs.clear()
                        songs.addAll(response.body()?.results!!)
                    searchResultsAdapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                show(VisibilityState.NO_CONNECTIONS)
            }
        })
    }
}