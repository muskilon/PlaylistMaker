package com.example.playlistmaker.search.data

import com.example.playlistmaker.player.data.CurrentTrackStorage
import com.example.playlistmaker.player.data.db.AppDatabase
import com.example.playlistmaker.search.domain.Resource
import com.example.playlistmaker.search.domain.SearchHistory
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val networkClient: NetworkClient,
    private val historySharedPreferences: HistorySharedPreferences,
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
        term: String
    ): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(SearchRequest(ENTITY, term, LANG))
        val favorites = appDatabase.songsDao().getTrackIdList()
        when (response.resultCode) {
            OK -> {
                with(response as SearchResponse) {
                    val data = results.filterNot { it.previewUrl.isNullOrEmpty() }.map {
                        Track(
                            trackId = it.trackId,
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTime = it.trackTime,
                            artworkUrl100 = it.artworkUrl100,
                            artworkUrl512 = it.artworkUrl512,
                            previewUrl = it.previewUrl!!,
                            collectionName = it.collectionName,
                            country = it.country,
                            primaryGenreName = it.primaryGenreName,
                            year = it.year,
                            isFavorites = (favorites.contains(it.trackId))
                        )
                    }
                    emit(Resource.Data(data))
                }
            }

            in NOT_FOUND -> emit(Resource.NotFound("not_found"))
            else -> {
                emit(Resource.ConnectionError("connection_error"))
            }
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        private const val OK = 200
        private val NOT_FOUND = listOf(400, 404)
        private const val ENTITY = "song"
        private const val LANG = "ru"

    }
}