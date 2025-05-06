    package com.example.doctruyen.entity

    import androidx.room.Entity
    import androidx.room.PrimaryKey
    import androidx.room.ForeignKey

    @Entity(
        tableName = "views",
        foreignKeys = [
            ForeignKey(
                entity = Story::class,
                parentColumns = ["id"],
                childColumns = ["story_id"],
                onDelete = ForeignKey.CASCADE
            )
        ]
    )
    data class View(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val story_id: Int,
        val views: Int
    )

