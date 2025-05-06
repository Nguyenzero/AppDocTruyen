    package com.example.doctruyen.tutruyen

    import android.app.Application
    import android.util.Log
    import androidx.lifecycle.AndroidViewModel
    import androidx.lifecycle.LiveData
    import com.example.doctruyen.database.AppDatabase
    import com.example.doctruyen.entity.Story

    class TuTruyenViewModel(application: Application, private val userId: Int) : AndroidViewModel(application) {
        private val bookmarkDao = AppDatabase.getDatabase(application).bookmarkDao()

        val bookmarkedStories: LiveData<List<Story>> = bookmarkDao.getReadingHistoryLiveData(userId).also {
            it.observeForever { list ->
                Log.d("TuTruyenViewModel", "Truyện của user $userId: ${list.map { s -> s.title }}")
            }
        }
    }

