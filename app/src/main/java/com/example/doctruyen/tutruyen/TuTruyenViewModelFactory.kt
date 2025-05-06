package com.example.doctruyen.tutruyen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TuTruyenViewModelFactory(
    private val application: Application,
    private val userId: Int
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TuTruyenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TuTruyenViewModel(application, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

