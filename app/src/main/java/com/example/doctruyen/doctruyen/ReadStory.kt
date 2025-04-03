package com.example.doctruyen.doctruyen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.Bookmark
import com.example.doctruyen.entity.Chapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReadStory : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnNext: ImageButton

    private lateinit var database: AppDatabase
    private var danhSachChuong: List<Chapter> = listOf()
    private var currentChapterIndex = 0
    private var userId = 1 // Giả sử lấy userId từ session hoặc truyền qua Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.doc_truyen)

        tvTitle = findViewById(R.id.tvTitle)
        tvContent = findViewById(R.id.tvContent)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        val rootView = findViewById<View>(R.id.rootView)


        rootView.setOnApplyWindowInsetsListener { v, insets ->
            v.setPadding(0, insets.systemWindowInsetTop, 0, 0)
            insets
        }

        val chuongId = intent.getIntExtra("CHUONG_ID", -1)
        val fromTuTruyen = intent.getBooleanExtra("FROM_TU_TRUYEN", false) // Kiểm tra nguồn mở

        if (chuongId == -1) {
            finish()
            return
        }

        database = Room.databaseBuilder(this, AppDatabase::class.java, "doctruyen_db")
            .fallbackToDestructiveMigration()
            .build()

        lifecycleScope.launch(Dispatchers.IO) {
            val chapterIdToOpen = if (fromTuTruyen) {
                val bookmark = database.bookmarkDao().getBookmark(userId, chuongId)
                // Thêm log để kiểm tra chapterIdToOpen
                Log.d("ReadStory", "Từ 'TuTruyen': bookmark cho userId=$userId, chuongId=$chuongId, chapterIdToOpen=${bookmark?.chapterId}")
                bookmark?.chapterId ?: chuongId
            } else {
                Log.d("ReadStory", "Không phải từ 'TuTruyen': chuongIdToOpen=$chuongId")
                chuongId
            }

            val chapter = database.chapterDao().getChapterById(chapterIdToOpen)
            val chapters = database.chapterDao().getChaptersByStoryId(chapter.storyId)

            withContext(Dispatchers.Main) {
                danhSachChuong = chapters
                currentChapterIndex = danhSachChuong.indexOfFirst { it.id == chapterIdToOpen }
                // Thêm log để kiểm tra chapter ID và currentChapterIndex
                Log.d("ReadStory", "Đã tải chapterIdToOpen=$chapterIdToOpen, currentChapterIndex=$currentChapterIndex")
                hienThiChuong(currentChapterIndex)
            }
        }

        btnPrevious.setOnClickListener { chuyenChuong(-1) }
        btnNext.setOnClickListener { chuyenChuong(1) }
    }

    private fun hienThiChuong(index: Int) {
        if (index in danhSachChuong.indices) {
            val chapter = danhSachChuong[index]

            tvTitle.text = "Chương ${chapter.chapterNumber}: ${chapter.title}"
            tvContent.text = chapter.content
            currentChapterIndex = index

            val sharedPreferences = getSharedPreferences("ReadStoryPrefs", MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putInt("LAST_READ_CHAPTER", chapter.id)
                apply()
            }
        }
    }

    private fun chuyenChuong(direction: Int) {
        val newIndex = currentChapterIndex + direction
        if (newIndex in danhSachChuong.indices) {
            hienThiChuong(newIndex)
        } else {
            Toast.makeText(this, "Không còn chương nào", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        saveBookmarkToDatabase()
    }

    private fun saveBookmarkToDatabase() {
        val sharedPreferences = getSharedPreferences("ReadStoryPrefs", MODE_PRIVATE)
        val lastReadChapterId = sharedPreferences.getInt("LAST_READ_CHAPTER", -1)

        if (lastReadChapterId != -1) {
            lifecycleScope.launch(Dispatchers.IO) {
                val chapter = database.chapterDao().getChapterById(lastReadChapterId)
                val bookmarkDao = database.bookmarkDao()
                val existingBookmark = bookmarkDao.getBookmark(userId, chapter.storyId)


                if (existingBookmark != null) {
                    bookmarkDao.updateBookmark(userId, chapter.storyId, chapter.id)

                } else {
                    val newBookmark = Bookmark(
                        userId = userId,
                        storyId = chapter.storyId,
                        chapterId = chapter.id
                    )
                    bookmarkDao.insertBookmark(newBookmark)

                }
            }
        }
    }
}
