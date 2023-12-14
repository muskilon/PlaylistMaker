package com.example.playlistmaker
//
//import android.annotation.SuppressLint
//import com.example.playlistmaker.data.dto.SearchResponse
//import com.example.playlistmaker.domain.models.Track
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//
//object Search {
//    val songs = ArrayList<Track>()
//    fun SearchActivity.search(queryInput: String) {
//        show(VisibilityManager.PROGRESS_BAR)
//        itunesService.getSearch("song", queryInput, "ru")
//            .enqueue(object : Callback<SearchResponse> {
//                @SuppressLint("NotifyDataSetChanged")
//                override fun onResponse(
//                    call: Call<SearchResponse>, response: Response<SearchResponse>
//                ) {
//                    if (response.body()?.resultCount == 0 || response.body() == null) {
//                        show(VisibilityManager.NOT_FOUND)
//                    } else {
//                        show(VisibilityManager.SEARCH)
//                        songs.clear()
//                        songs.addAll(response.body()?.results!!)
//                    searchResultsAdapter.notifyDataSetChanged()
//                }
//            }
//            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
//                show(VisibilityManager.NO_CONNECTIONS)
//            }
//        })
//    }
//}