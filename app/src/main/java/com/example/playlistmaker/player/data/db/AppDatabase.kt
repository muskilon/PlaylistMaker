package com.example.playlistmaker.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.search.domain.Track

@Database(
    version = 7,
    entities = [SongsEntity::class, PlayListEntity::class, Track::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): SongsDao
    abstract fun playListsDao(): PlayListsDao
}