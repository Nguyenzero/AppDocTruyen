package com.example.doctruyen.doctruyen

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.*
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReadStory : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnChapterList: ImageButton


    private lateinit var database: AppDatabase
    private var danhSachChuong: List<Chapter> = listOf()
    private var currentChapterIndex = 0
    private var userId: Int = -1
    private var storyId: Int = -1
    private var storyTitle: String = "" // lưu tên truyện

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.doc_truyen)

        tvTitle = findViewById(R.id.tvTitle)
        tvContent = findViewById(R.id.tvContent)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)
        btnChapterList = findViewById(R.id.btnChapterList)


        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        userId = intent.getIntExtra("USER_ID", -1)
        val chuongId = intent.getIntExtra("CHUONG_ID", -1)
        storyId = intent.getIntExtra("STORY_ID", -1)
        storyTitle = intent.getStringExtra("STORY_TITLE") ?: "Tên truyện không xác định"


        val fromTuTruyen = intent.getBooleanExtra("FROM_TU_TRUYEN", false)

        if (userId == -1 || chuongId == -1 || storyId == -1) {
            Toast.makeText(this, "Thiếu dữ liệu để đọc truyện", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        database = Room.databaseBuilder(this, AppDatabase::class.java, "doctruyen1")
            .fallbackToDestructiveMigration()
            .build()

        lifecycleScope.launch(Dispatchers.IO) {
            val chapterIdToOpen = if (fromTuTruyen) {
                database.bookmarkDao().getBookmark(userId, storyId)?.chapterId ?: chuongId
            } else {
                chuongId
            }

            val chapter = database.chapterDao().getChapterById(chapterIdToOpen)
            val chapters = database.chapterDao().getChaptersByStoryId(chapter.storyId)

            withContext(Dispatchers.Main) {
                danhSachChuong = chapters
                currentChapterIndex = danhSachChuong.indexOfFirst { it.id == chapterIdToOpen }

                hienThiChuong(currentChapterIndex)
            }
        }

        btnPrevious.setOnClickListener { chuyenChuong(-1) }
        btnNext.setOnClickListener { chuyenChuong(1) }
        btnChapterList.setOnClickListener { showChapterList() }

        tvContent.setOnLongClickListener {
            val chapterTitle = tvTitle.text.toString()
            val content = tvContent.text.toString()

            // Log ID chương đang đọc
            Log.d("ReadStory", "ID chương đang đọc: ${danhSachChuong[currentChapterIndex].id}")

            // Truyền id truyện và số chương truyện vào fragment
            ReadSettingsFragment().apply {
                arguments = Bundle().apply {

                    putInt("storyId", storyId) // Thêm id truyện
                    putInt("chapterId", danhSachChuong[currentChapterIndex].id) // Thêm số chương hiện tại
                }
                show(supportFragmentManager, "ReadSettings")
            }
            true
        }


    }

    private fun hienThiChuong(index: Int) {
        if (index in danhSachChuong.indices) {
            val chapter = danhSachChuong[index]
            tvTitle.text = "Chương ${chapter.chapterNumber}: ${chapter.title}"
            tvContent.text = chapter.content
            currentChapterIndex = index

            saveBookmarkToDatabase(chapter)
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

    private fun saveBookmarkToDatabase(chapter: Chapter) {
        lifecycleScope.launch(Dispatchers.IO) {
            val bookmarkDao = database.bookmarkDao()
            val existingBookmark = bookmarkDao.getBookmark(userId, chapter.storyId)
            if (existingBookmark != null) {
                bookmarkDao.updateBookmark(userId, chapter.storyId, chapter.id)
            } else {
                bookmarkDao.insertBookmark(
                    Bookmark(userId = userId, storyId = chapter.storyId, chapterId = chapter.id)
                )
            }
        }
    }

    fun thayDoiMauNen(colorHex: String) {
        tvContent.setBackgroundColor(Color.parseColor(colorHex))
    }

    fun thayDoiCoChu(size: Float) {
        tvContent.textSize = size
    }

    fun thayDoiFontChu(fontName: String) {
        tvContent.typeface = when (fontName) {
            "Sans Serif" -> Typeface.SANS_SERIF
            "Serif" -> Typeface.SERIF
            "Monospace" -> Typeface.MONOSPACE
            else -> Typeface.DEFAULT
        }
    }

    private fun showChapterList() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_chapter_list, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewChapters)

        val chapterTitles = danhSachChuong.map { "Chương ${it.chapterNumber}: ${it.title}" }
        val adapter = ChapterListAdapter(chapterTitles) { selectedIndex ->
            hienThiChuong(selectedIndex)
            bottomSheetDialog.dismiss()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }
}
