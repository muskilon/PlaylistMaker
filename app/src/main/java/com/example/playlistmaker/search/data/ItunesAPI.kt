package com.example.playlistmaker.search.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesAPI {
    @GET(".")
    suspend fun getSearch(
        @Query("entity") entity: String,
        @Query("term") term: String,
        @Query("lang") lang: String
    ): SearchResponse
}