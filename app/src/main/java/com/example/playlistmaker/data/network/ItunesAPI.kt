package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesAPI {
    @GET(".")
    fun getSearch(
        @Query("entity") entity: String,
        @Query("term") term: String,
        @Query("lang") lang: String
    ): Call<SearchResponse>
}