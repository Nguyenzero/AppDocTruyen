package com.example.doctruyen.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "chapters",
    foreignKeys = [ForeignKey(
        entity = Story::class,
        parentColumns = ["id"],
        childColumns = ["storyId"],
        onDelete = ForeignKey.CASCADE // Xóa truyện sẽ xóa luôn các chương
    )]
)
data class Chapter(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID chương
    val storyId: Int, // ID truyện (liên kết với bảng Story)
    val chapterNumber: Int, // Số chương
    val title: String, // Tiêu đề chương
    val content: String // Nội dung chương
)
