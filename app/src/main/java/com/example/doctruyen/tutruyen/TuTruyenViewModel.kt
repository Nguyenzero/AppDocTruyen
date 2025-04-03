package com.example.doctruyen.tutruyen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.Story

class TuTruyenViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val bookmarkDao = database.bookmarkDao()

    // LiveData để quan sát danh sách truyện đã bookmark
    val bookmarkedStories: LiveData<List<Story>> = bookmarkDao.getReadingHistoryLiveData(1)
}
