    package com.example.doctruyen.entity

    import androidx.room.Entity
    import androidx.room.PrimaryKey

    @Entity(tableName = "users")
    data class User(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val email: String,
        val password: String,
        val role: String, // Thêm cột vai trò
        val username: String // Thêm cột tên người dùng
    )
