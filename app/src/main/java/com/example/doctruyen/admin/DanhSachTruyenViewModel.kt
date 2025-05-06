package com.example.doctruyen.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctruyen.dao.DanhGiaDao
import com.example.doctruyen.dao.StoryDao
import com.example.doctruyen.entity.Story
import kotlinx.coroutines.launch

class DanhSachTruyenViewModel(
    private val storyDao: StoryDao,
    private val danhGiaDao: DanhGiaDao
) : ViewModel() {

    private val _truyenList = MutableLiveData<List<Story>>()
    val truyenList: LiveData<List<Story>> get() = _truyenList

    fun loadTruyenCoDanhGia() {
        viewModelScope.launch {
            val ids = danhGiaDao.getRatedStoryIds()
            val stories = mutableListOf<Story>()
            for (id in ids) {
                val story = storyDao.getStoryById(id)
                if (story != null) stories.add(story)
            }
            _truyenList.postValue(stories)
        }
    }
}
