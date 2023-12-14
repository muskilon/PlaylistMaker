package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.SearchRequest
import com.example.playlistmaker.data.dto.SearchResponse
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchSongs(entity: String, term: String, lang: String): List<Track> {
        val response = networkClient.doRequest(SearchRequest(entity, term, lang))
        if (response.resultCode == 200) {
            return (response as SearchResponse).results.map {
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
            }
        } else {
            return emptyList()
        }
    }
}