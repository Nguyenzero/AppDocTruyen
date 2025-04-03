package com.example.doctruyen.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.doctruyen.entity.Chapter

@Dao
interface ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: Chapter)

    @Query("SELECT * FROM chapters WHERE storyId = :storyId")
    fun getChaptersByStoryId(storyId: Int): List<Chapter>

    @Delete
    suspend fun deleteChapter(chapter: Chapter)

    @Query("SELECT COUNT(*) FROM chapters WHERE storyId = :storyId")
    suspend fun getChapterCountByStoryId(storyId: Int): Int

    @Query("SELECT * FROM chapters WHERE id = :chapterId")
    fun getChapterById(chapterId: Int): Chapter

    @Query("SELECT id FROM chapters WHERE storyId = :storyId ORDER BY chapterNumber ASC LIMIT 1")
    suspend fun getFirstChapterId(storyId: Int): Int?


}
