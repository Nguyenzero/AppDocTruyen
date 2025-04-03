package com.example.doctruyen.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories")
data class Story(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID truyện
    val title: String, // Tên truyện
    val author: String, // Tác giả
    val genre: String, // Thể loại
    val coverImage: String, // Ảnh bìa (đường dẫn ảnh)
    val description: String, // Mô tả nội dung
    val status: String, // Trạng thái (Đang ra, Hoàn thành)

)

