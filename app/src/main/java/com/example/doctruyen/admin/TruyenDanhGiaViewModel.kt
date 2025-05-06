package com.example.doctruyen.admin

import android.app.Application
import androidx.lifecycle.*
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.DanhGia
import com.example.doctruyen.entity.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TruyenDanhGiaViewModel(application: Application) : AndroidViewModel(application) {
    private val storyDao = AppDatabase.getDatabase(application).storyDao()
    private val danhGiaDao = AppDatabase.getDatabase(application).danhGiaDao()

    private val _listStoryWithReview = MutableLiveData<List<Pair<Story, Int>>>()
    val listStoryWithReview: LiveData<List<Pair<Story, Int>>> = _listStoryWithReview



    fun loadStoriesWithReviewCount() {
        viewModelScope.launch(Dispatchers.IO) {
            val ratedStoryIds = danhGiaDao.getRatedStoryIds()
            val result = mutableListOf<Pair<Story, Int>>()
            for (id in ratedStoryIds) {
                val story = storyDao.getStoryById(id)
                val soDanhGia = danhGiaDao.getDanhGiaCountByStoryId(id)
                if (story != null) {
                    result.add(Pair(story, soDanhGia))
                }
            }
            _listStoryWithReview.postValue(result)
        }
    }
}






