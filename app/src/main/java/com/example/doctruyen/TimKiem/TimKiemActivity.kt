package com.example.doctruyen.TimKiem

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimKiemActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var btnFilter: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StoryAdapter
    private lateinit var db: AppDatabase
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tim_kiem)

        etSearch = findViewById(R.id.etSearch)
        btnFilter = findViewById(R.id.btnFilter)
        recyclerView = findViewById(R.id.recyclerViewSearchResults)

        recyclerView.layoutManager = LinearLayoutManager(this)
        userId = intent.getIntExtra("USER_ID", -1)
        adapter = StoryAdapter(userId,mutableListOf())
        recyclerView.adapter = adapter
        db = AppDatabase.getDatabase(applicationContext)

        btnFilter.setOnClickListener {
            val storyDao = db.storyDao()
            val bottomSheet = FilterBottomSheetFragment(storyDao) { sort: String?, status: String?, genre: String?, author: String? ->
                filterStories(sort, status, genre, author)
            }
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }


        displayAllStories()
        setupSearchListener()


    }
    private fun filterStories(sort: String?, status: String?, genre: String?, author: String?) {


        lifecycleScope.launch {
            db.storyDao().getAllStories().collectLatest { storyList ->
                var filtered = storyList

                if (!status.isNullOrEmpty()) {
                    filtered = filtered.filter { it.status.equals(status, ignoreCase = true) }
                }
                if (!genre.isNullOrEmpty()) {
                    filtered = filtered.filter { it.genre.equals(genre, ignoreCase = true) }

                }
                if (!author.isNullOrEmpty()) {
                    filtered = filtered.filter { it.author.equals(author, ignoreCase = true) }

                }

                val enrichedList = withContext(Dispatchers.IO) {
                    val enriched = filtered.map { story ->
                        val views = db.viewDao().getViewCountByStoryId(story.id) ?: 0
                        val ratings = db.danhGiaDao().getDanhGiaByStoryId(story.id)
                        val avgRating = if (ratings.isNotEmpty()) {
                            ratings.map { it.rank }.average().toFloat()
                        } else 0f
                        Triple(story, views, avgRating)
                    }

                    when (sort) {
                        "Lượt đọc" -> enriched.sortedByDescending { it.second }
                        "Số sao" -> enriched.sortedByDescending { it.third }
                        "Đề cử" -> enriched
                        else -> enriched
                    }
                }

                adapter.updateData(enrichedList)
            }
        }
    }





    private fun displayAllStories() {
        lifecycleScope.launch {
            db.storyDao().getAllStories().collectLatest { storyList ->
                val enrichedList = withContext(Dispatchers.IO) {
                    storyList.map { story ->
                        val views = db.viewDao().getViewCountByStoryId(story.id) ?: 0
                        val ratings = db.danhGiaDao().getDanhGiaByStoryId(story.id)
                        val avgRating = if (ratings.isNotEmpty()) {
                            ratings.map { it.rank }.average().toFloat()
                        } else 0f
                        Triple(story, views, avgRating)
                    }
                }
                adapter.updateData(enrichedList)
            }
        }
    }
    private fun setupSearchListener() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                if (query.isEmpty()) {
                    displayAllStories()
                } else {
                    searchStories(query)
                }
            }
        })
    }
    private fun searchStories(query: String) {
        lifecycleScope.launch {
            db.storyDao().searchStoriesByTitle("%$query%").collectLatest { storyList ->
                val enrichedList = withContext(Dispatchers.IO) {
                    storyList.map { story ->
                        val views = db.viewDao().getViewCountByStoryId(story.id) ?: 0
                        val ratings = db.danhGiaDao().getDanhGiaByStoryId(story.id)
                        val avgRating = if (ratings.isNotEmpty()) {
                            ratings.map { it.rank }.average().toFloat()
                        } else 0f
                        Triple(story, views, avgRating)
                    }
                }
                adapter.updateData(enrichedList)
            }
        }
    }

}