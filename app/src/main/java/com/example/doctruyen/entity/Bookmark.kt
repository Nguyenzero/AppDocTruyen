package com.example.doctruyen.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookmarks",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Story::class, parentColumns = ["id"], childColumns = ["storyId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Chapter::class, parentColumns = ["id"], childColumns = ["chapterId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID bookmark
    val userId: Int, // ID người dùng
    val storyId: Int, // ID truyện
    val chapterId: Int // ID của chương đang đọc
)

