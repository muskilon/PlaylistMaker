package com.example.playlistmaker.search.data

import com.example.playlistmaker.player.data.CurrentTrackStorage
import com.example.playlistmaker.search.domain.Resource
import com.example.playlistmaker.search.domain.SearchHistory
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val historySharedPreferences: HistorySharedPreferences
) : TrackRepository {

    override fun setCurrentTrack(currentTrack: Track) {
        CurrentTrackStorage.setCurrentTrack(currentTrack)
    }

    override fun readHistory(): SearchHistory {
        return historySharedPreferences.readHistory()
    }

    override fun writeHistory(songsHistory: SearchHistory) {
        historySharedPreferences.writeHistory(songsHistory)
    }

    override fun clearHistory() {
        historySharedPreferences.clearHistory()
    }

    override fun searchSongs(
        entity: String,
        term: String,
        lang: String
    ): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(SearchRequest(entity, term, lang))
        when (response.resultCode) {
            200 -> {
                with(response as SearchResponse) {
                    val data = results.filterNot { it.previewUrl.isNullOrEmpty() }.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTime,
                            it.artworkUrl100,
                            it.artworkUrl512,
                            it.previewUrl!!,
                            it.collectionName,
                            it.country,
                            it.primaryGenreName,
                            it.year
                        )
                    }
                    emit(Resource.Data(data))
                }
            }

            400, 404 -> emit(Resource.NotFound("not_found"))
            else -> {
                emit(Resource.ConnectionError("connection_error"))
            }
        }
    }
}