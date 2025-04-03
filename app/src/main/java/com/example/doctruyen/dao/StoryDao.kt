package com.example.doctruyen.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow
import com.example.doctruyen.entity.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: Story)

    @Query("SELECT * FROM stories")
    fun getAllStories(): Flow<List<Story>>

    @Query("SELECT * FROM stories WHERE id = :storyId")
    suspend fun getStoryById(storyId: Int): Story?

    @Query("DELETE FROM stories WHERE id = :storyId")
    suspend fun deleteStory(storyId: Int)  // Thêm hàm xóa truyện



 @Query("SELECT * FROM stories ORDER BY id DESC LIMIT 1")
 fun getLatestStory(): Flow<Story>

    @Query("SELECT * FROM stories ORDER BY id DESC")
    fun getAllStoriesNewestFirst(): Flow<List<Story>>





}
