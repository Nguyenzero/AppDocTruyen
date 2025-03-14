package com.example.doctruyen.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.doctruyen.entity.Bookmark

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark)

    @Query("SELECT * FROM bookmarks WHERE userId = :userId")
    suspend fun getBookmarksByUserId(userId: Int): List<Bookmark>
}
