package com.example.playlistmaker.search.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val context: Context,
    private val iTunesService: ItunesAPI
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return try {
            if (!isConnected()) {
                return Response().apply { resultCode = -1 }
            }
            if (dto is SearchRequest) {
                withContext(Dispatchers.IO) {
                    val response = iTunesService.getSearch(dto.entity, dto.term, dto.lang)
                    response.apply { resultCode = 200 }
                }
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