package com.example.doctruyen.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.doctruyen.entity.Chapter

@Dao
interface ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: Chapter)

    @Query("SELECT * FROM chapters WHERE storyId = :storyId")
    suspend fun getChaptersByStoryId(storyId: Int): List<Chapter>
}
