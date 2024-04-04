package com.example.playlistmaker.player.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlayListsDao {

    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayList(playList: PlayListEntity)

    @Insert(entity = PlayListTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToPlayList(track: PlayListTrackEntity)

    @Query("SELECT * FROM playlists_table")
    suspend fun getListOfPlayLists(): List<PlayListEntity>

    @Query("SELECT * FROM playlist_songs_table WHERE playListId = :playListId")
    suspend fun getTracksFromPlayList(playListId: Long): List<PlayListTrackEntity>

    @Delete(entity = PlayListEntity::class)
    fun deletePlayList(playList: PlayListEntity) //TODO еще надо и треки удалять

    @Delete(entity = PlayListTrackEntity::class)
    fun deleteTrackFromPlayList(track: PlayListTrackEntity)
}