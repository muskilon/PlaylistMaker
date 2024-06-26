package com.example.playlistmaker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.search.domain.Track

@Dao
interface PlayListsDao {

    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlayList(playList: PlayListEntity)

    @Update(entity = PlayListEntity::class)
    suspend fun updatePlayList(playList: PlayListEntity)

    @Insert(entity = Track::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlayListStorage(track: Track)

    @Query("SELECT * FROM playlists_table")
    suspend fun getAllPlayLists(): List<PlayListEntity>

    @Query("SELECT * FROM playlist_songs_table")
    suspend fun getAllPlayListsTracks(): List<Track>

    @Delete(entity = PlayListEntity::class)
    fun deletePlayList(playList: PlayListEntity) //TODO еще надо и треки удалять

    @Query("DELETE FROM playlist_songs_table WHERE trackId in (:trackList)")
    fun deleteTrackFromPlayListStorage(trackList: List<String>)
}