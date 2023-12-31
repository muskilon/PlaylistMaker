package com.example.playlistmaker.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com/search/"

    private val retrofit =
        Retrofit.Builder().baseUrl(iTunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val iTunesService = retrofit.create(ItunesAPI::class.java)

    override fun doRequest(dto: Any): Response {
        return try {
            if (dto is SearchRequest) {
                val resp = iTunesService.getSearch(dto.entity, dto.term, dto.lang).execute()
                val body = resp.body() ?: Response()

                body.apply { resultCode = resp.code() }
            } else {
                Response().apply { resultCode = 400 }
            }
        } catch (ex: Exception) {
            Response().apply { resultCode = 500 }
        }
    }
}