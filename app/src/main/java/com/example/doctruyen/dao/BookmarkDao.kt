package com.example.doctruyen.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.doctruyen.entity.Bookmark
import com.example.doctruyen.entity.Story

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmarks WHERE userId = :userId AND storyId = :storyId")
    suspend fun deleteBookmark(userId: Int, storyId: Int)

    @Query("SELECT * FROM bookmarks WHERE userId = :userId AND storyId = :storyId LIMIT 1")
    suspend fun getBookmark(userId: Int, storyId: Int): Bookmark?

    @Query("UPDATE bookmarks SET chapterId = :chapterId WHERE userId = :userId AND storyId = :storyId")
    suspend fun updateBookmark(userId: Int, storyId: Int, chapterId: Int)

    @Query("""
        SELECT stories.* FROM stories 
        INNER JOIN bookmarks ON stories.id = bookmarks.storyId 
        WHERE bookmarks.userId = :userId
    """)
    fun getReadingHistoryLiveData(userId: Int): LiveData<List<Story>>

    @Query("""
        SELECT c.chapterNumber FROM chapters c
        INNER JOIN bookmarks b ON c.id = b.chapterId
        WHERE b.userId = :userId AND b.storyId = :storyId
        LIMIT 1
    """)
    suspend fun getLastReadChapterNumber(userId: Int, storyId: Int): Int?

    @Query("""
    SELECT stories.* FROM stories 
    INNER JOIN bookmarks ON stories.id = bookmarks.storyId 
    WHERE bookmarks.userId = :userId
""")
    suspend fun getBookmarkedStories(userId: Int): List<Story>

}



