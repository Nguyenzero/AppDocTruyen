package com.example.doctruyen.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.doctruyen.dao.*
import com.example.doctruyen.entity.*

@Database(
    entities = [User::class, Story::class, Chapter::class, Bookmark::class],
    version = 1 // Đây là lần đầu tạo database
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun storyDao(): StoryDao
    abstract fun chapterDao(): ChapterDao
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "doctruyen_db"
                ).fallbackToDestructiveMigration() // Nếu có thay đổi schema, nó sẽ xóa database cũ và tạo lại
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
