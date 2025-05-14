package com.example.doctruyen.TimKiem

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.doctruyen.dao.StoryDao

class FilterViewModel(private val storyDao: StoryDao) : ViewModel() {


    val allGenres: LiveData<List<String>> = storyDao.getAllGenres().asLiveData()
    val allAuthors: LiveData<List<String>> = storyDao.getAllAuthors().asLiveData()
}