package com.example.playlistmaker.player.data.db

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

//    @Query("SELECT * FROM playlist_songs_table WHERE playListId = :playListId")
//    suspend fun getTracksFromPlayList(playListId: Long): List<PlayListTrackEntity>

    @Delete(entity = PlayListEntity::class)
    fun deletePlayList(playList: PlayListEntity) //TODO еще надо и треки удалять

    @Delete(entity = Track::class)
    fun deleteTrackFromPlayList(track: Track)
}