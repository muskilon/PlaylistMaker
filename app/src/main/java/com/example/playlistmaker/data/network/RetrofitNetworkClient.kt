package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.SearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val imdbBaseUrl = "https://itunes.apple.com/search/"

    private val retrofit =
        Retrofit.Builder().baseUrl(imdbBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val imdbService = retrofit.create(ItunesAPI::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is SearchRequest) {
            val resp = imdbService.getSearch(dto.entity, dto.term, dto.lang).execute()
            val body = resp.body() ?: Response()
            if (resp.body()?.resultCount == 0 || resp.body() == null) {
                return Response().apply { resultCode = 400 }
            } else return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}