package com.example.doctruyen.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "rankings",
    foreignKeys = [
        ForeignKey(
            entity = Story::class,
            parentColumns = ["id"],
            childColumns = ["story_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserRanking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID của liên kết giữa người dùng và truyện
    val story_id: Int, // ID của truyện
    val rank: Int, // Rank của người dùng với truyện
    val views: Int, // Số lượt xem của truyện
    val favorites: Int // Số lượt thêm vào yêu thích của người dùng
)
