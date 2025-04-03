package com.example.doctruyen.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.doctruyen.entity.UserRanking

@Dao
interface UserRankingDao {

    @Insert
    suspend fun insertUserStory(userStory: UserRanking)

    @Query("SELECT * FROM rankings WHERE story_id = :storyId")
    suspend fun getUserStoryByStoryId(storyId: Int): List<UserRanking>?

    @Query("UPDATE RANKINGS SET rank = :rank, views = :views, favorites = :favorites WHERE story_id = :storyId")
    suspend fun updateUserStory(storyId: Int, rank: Int, views: Int, favorites: Int)

    @Query("DELETE FROM rankings WHERE story_id = :storyId")
    suspend fun deleteUserStory(storyId: Int)
}
