package com.example.doctruyen.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "danhgia",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Story::class,
            parentColumns = ["id"],
            childColumns = ["story_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DanhGia(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val user_id: Int,
    val story_id: Int,
    val rank: Float,
    val comment: String,
    val time: Long = System.currentTimeMillis(),
    val likes: Int = 0
)

