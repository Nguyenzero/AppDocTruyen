package com.example.doctruyen.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.doctruyen.entity.DanhGia

@Dao
interface DanhGiaDao {
    @Insert
    suspend fun insertDanhGia(danhGia: DanhGia)
    @Delete
    suspend fun delete(danhGia: DanhGia)

    @Query("SELECT * FROM danhgia WHERE story_id = :storyId")
    fun getDanhGiaTheoTruyen(storyId: Int): LiveData<List<DanhGia>>

    @Query("SELECT COUNT(*) FROM danhgia WHERE story_id = :storyId")
    suspend fun getDanhGiaCountByStoryId(storyId: Int): Int

    @Query("SELECT * FROM danhgia WHERE story_id = :storyId")
    suspend fun getDanhGiaByStoryId(storyId: Int): List<DanhGia>


    @Query("SELECT * FROM danhgia WHERE story_id = :storyId ORDER BY time DESC")
    fun getDanhGiaTheoTruyenMoi(storyId: Int): List<DanhGia>

    @Query("SELECT * FROM danhgia WHERE story_id = :storyId ORDER BY time ASC")
    fun getDanhGiaTheoTruyenCu(storyId: Int): List<DanhGia>

    @Query("SELECT AVG(rank) FROM danhgia WHERE story_id = :storyId")
    fun getLiveAverageRatingByStoryId(storyId: Int): LiveData<Float?>


    @Query("SELECT DISTINCT story_id FROM danhgia")
    suspend fun getRatedStoryIds(): List<Int>


    @Query("SELECT * FROM danhgia")
    fun getAllDanhGia(): kotlinx.coroutines.flow.Flow<List<DanhGia>>




}
