package com.example.playlistmaker.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com/search/"

    private val retrofit =
        Retrofit.Builder().baseUrl(iTunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val iTunesService = retrofit.create(ItunesAPI::class.java)

    override fun doRequest(dto: Any): Response {
        return try {
            if (!isConnected()) {
                return Response().apply { resultCode = -1 }
            }
            if (dto is SearchRequest) {
                val response = iTunesService.getSearch(dto.entity, dto.term, dto.lang).execute()
                val body = response.body() ?: Response()

                body.apply { resultCode = response.code() }
            } else {
                Response().apply { resultCode = 400 }
            }
        } catch (ex: Exception) {
            Response().apply { resultCode = 500 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}