package com.example.doctruyen.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.doctruyen.dao.ChapterDao
import com.example.doctruyen.dao.DanhGiaDao
import com.example.doctruyen.dao.StoryDao
import com.example.doctruyen.dao.UserDao
import com.example.doctruyen.database.AppDatabase
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao: UserDao = AppDatabase.getDatabase(application).userDao()
    private val storyDao: StoryDao = AppDatabase.getDatabase(application).storyDao()
    private val danhGiaDao: DanhGiaDao = AppDatabase.getDatabase(application).danhGiaDao()
    private val chapterDao: ChapterDao = AppDatabase.getDatabase(application).chapterDao()

    private val _totalUsers = MutableLiveData<Int>()
    val totalUsers: LiveData<Int> get() = _totalUsers

    private val _totalStories = MutableLiveData<Int>()
    val totalStories: LiveData<Int> get() = _totalStories

    private val _totalRatings = MutableLiveData<Int>()
    val totalRatings: LiveData<Int> get() = _totalRatings

    private val _totalChapters = MutableLiveData<Int>()
    val totalChapters: LiveData<Int> get() = _totalChapters

    init {
        getTotalStories()
        getTotalUsers()
        getTotalRatings()
        getTotalChapters()
    }

    private fun getTotalStories() {
        viewModelScope.launch {
            val total = storyDao.getTotalStoryCount()
            _totalStories.value = total
        }
    }

    private fun getTotalUsers() {
        viewModelScope.launch {
            val total = userDao.getTotalUserCount()
            _totalUsers.value = total
        }
    }

    private fun getTotalRatings() {
        viewModelScope.launch {
            // Giả sử storyId = 1, có thể thay đổi giá trị này tùy theo yêu cầu
            val total = danhGiaDao.getTotalRatingsByStoryId(1)
            _totalRatings.value = total
        }
    }

    private fun getTotalChapters() {
        viewModelScope.launch {
            // Giả sử storyId = 1, có thể thay đổi giá trị này tùy theo yêu cầu
            val total = chapterDao.getTotalChaptersByStoryId(1)
            _totalChapters.value = total
        }
    }
}



