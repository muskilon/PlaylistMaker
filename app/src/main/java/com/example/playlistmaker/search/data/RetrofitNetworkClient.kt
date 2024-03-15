package com.example.playlistmaker.search.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val context: Context,
    private val itunesAPI: ItunesAPI
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return try {
            if (!isConnected()) {
                return Response().apply { resultCode = -1 }
            }
            if (dto is SearchRequest) {
                withContext(Dispatchers.IO) {
                    val response = itunesAPI.getSearch(dto.entity, dto.term, dto.lang)
                    Log.d("RTF", response.results.toString())
                    response.apply { resultCode = OK }
                }
            } else {
                Response().apply { resultCode = NOT_FOUND }
            }
        } catch (ex: Exception) {
            Response().apply { resultCode = SERVER_ERROR }
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

    companion object {
        private const val OK = 200
        private const val NOT_FOUND = 400
        private const val SERVER_ERROR = 500
    }
}