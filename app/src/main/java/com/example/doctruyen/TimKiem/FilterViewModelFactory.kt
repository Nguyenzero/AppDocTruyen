package com.example.doctruyen.TimKiem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.doctruyen.dao.StoryDao

class FilterViewModelFactory(private val storyDao: StoryDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
            return FilterViewModel(storyDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}