package com.example.playlistmaker.data

import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.Track
import com.example.playlistmaker.domain.TrackRepository

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchSongs(entity: String, term: String, lang: String): Resource<List<Track>> {
        val response = networkClient.doRequest(SearchRequest(entity, term, lang))
        return when (response.resultCode) {
            200 -> Resource.Data((response as SearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTime,
                    it.artworkUrl100,
                    it.artworkUrl512,
                    it.previewUrl,
                    it.collectionName,
                    it.country,
                    it.primaryGenreName,
                    it.year
                )
            })

            400 -> Resource.NotFound("not_found")
            else -> {
                Resource.ConnectionError("connection_error")
            }
        }
    }
}