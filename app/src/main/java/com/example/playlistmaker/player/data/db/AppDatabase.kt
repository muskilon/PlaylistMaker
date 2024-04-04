package com.example.playlistmaker.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 4,
    entities = [SongsEntity::class, PlayListEntity::class, PlayListTrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songsDao(): SongsDao
    abstract fun playListsDao(): PlayListsDao
}