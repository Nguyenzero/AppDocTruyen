    package com.example.doctruyen.dao

    import androidx.room.Dao
    import androidx.room.Insert
    import androidx.room.OnConflictStrategy
    import androidx.room.Query
    import androidx.room.Delete
    import androidx.room.Update
    import kotlinx.coroutines.flow.Flow
    import com.example.doctruyen.entity.Story

    @Dao
    interface StoryDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertStory(story: Story)

        @Update
        suspend fun updateStory(story: Story)

        @Query("SELECT * FROM stories WHERE title LIKE :query")
        fun searchStoriesByTitle(query: String): kotlinx.coroutines.flow.Flow<List<Story>>


        @Query("SELECT * FROM stories")
        fun getAllStories(): Flow<List<Story>>


        @Query("SELECT DISTINCT genre FROM stories")
        fun getAllGenres(): Flow<List<String>>

        @Query("SELECT DISTINCT author FROM stories")
        fun getAllAuthors(): Flow<List<String>>

        @Query("SELECT DISTINCT status FROM stories")
        fun getAllStatuses(): Flow<List<String>>


        @Query("SELECT * FROM stories WHERE id = :storyId")
        suspend fun getStoryById(storyId: Int): Story?

        @Query("DELETE FROM stories WHERE id = :storyId")
        suspend fun deleteStory(storyId: Int)  // Thêm hàm xóa truyện



     @Query("SELECT * FROM stories ORDER BY id DESC LIMIT 1")
     fun getLatestStory(): Flow<Story>

        @Query("SELECT * FROM stories ORDER BY id DESC")
        fun getAllStoriesNewestFirst(): Flow<List<Story>>

        @Query("SELECT * FROM stories")
        fun getaddStories():  Flow<List<Story>>

        @Query("SELECT COUNT(*) FROM stories")
        suspend fun getTotalStoryCount(): Int




    }
