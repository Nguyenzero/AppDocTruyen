package com.example.doctruyen.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.doctruyen.dao.*
import com.example.doctruyen.entity.*
import com.example.doctruyen.entity.Bookmark
import com.example.doctruyen.entity.Chapter
import com.example.doctruyen.entity.Story
import com.example.doctruyen.entity.User
import com.example.doctruyen.entity.DanhGia
import com.example.doctruyen.entity.View
import com.example.doctruyen.database.AppDatabase


@Database(
    entities = [User::class, Story::class, Chapter::class, Bookmark::class, DanhGia::class, View::class],
    version = 2 // Tăng version lên 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun storyDao(): StoryDao
    abstract fun chapterDao(): ChapterDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun viewDao(): ViewDao
    abstract fun danhGiaDao(): DanhGiaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "doctruyen1"
                )

                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
