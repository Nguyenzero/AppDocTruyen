package com.example.doctruyen.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.doctruyen.entity.View


@Dao
interface ViewDao {

    @Insert
    suspend fun insertUserStory(userStory: View)

    @Query("SELECT * FROM views WHERE story_id = :storyId")
    suspend fun getUserStoryByStoryId(storyId: Int): List<View>?  // Lấy thông tin của truyện theo story_id

    @Query("UPDATE views SET views = :views WHERE story_id = :storyId")
    suspend fun updateUserStory(storyId: Int, views: Int)  // Cập nhật lượt xem cho truyện

    @Query("DELETE FROM views WHERE story_id = :storyId")
    suspend fun deleteUserStory(storyId: Int)

    @Query("UPDATE views SET views = views + 1 WHERE story_id = :storyId")
    suspend fun incrementViews(storyId: Int)


    @Query("SELECT * FROM views ORDER BY views DESC")
    fun getAllRankings(): LiveData<List<View>>  // Lấy tất cả các ranking, sắp xếp theo lượt xem
}



