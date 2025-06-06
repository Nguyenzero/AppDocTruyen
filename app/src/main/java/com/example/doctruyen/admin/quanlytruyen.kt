package com.example.doctruyen.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.Story
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
class quanlytruyen : AppCompatActivity() {

    private lateinit var recyclerTruyen: RecyclerView
    private lateinit var btnThemTruyen: Button
    private lateinit var btnBack: ImageView
    private lateinit var adapter: TruyenAdapter
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quan_ly_truyen)

        val rootView = findViewById<View>(R.id.quanlytruyen) // Thay rootView bằng ID của LinearLayout gốc
        rootView.setOnApplyWindowInsetsListener { v, insets ->
            v.setPadding(0, insets.systemWindowInsetTop, 0, 0) // Đẩy nội dung xuống dưới
            insets
        }

        recyclerTruyen = findViewById(R.id.recyclerTruyen)
        btnThemTruyen = findViewById(R.id.btnThemTruyen)
        btnBack = findViewById(R.id.btnBack)

        db = AppDatabase.getDatabase(this)

        adapter = TruyenAdapter(
            emptyList(),
            onItemClick = { story -> goToThemChuong(story) }, // Khi nhấn vào truyện
            onDelete = { story -> deleteStory(story) },
            onEdit = { story -> goToThemTruyen(story) } // Xử lý khi nhấn chỉnh sửa
        )

        recyclerTruyen.layoutManager = LinearLayoutManager(this)
        recyclerTruyen.adapter = adapter

        btnBack.setOnClickListener { finish() }

        btnThemTruyen.setOnClickListener {
            startActivity(Intent(this, themtruyen::class.java))
        }

        loadStories()
    }

    private fun loadStories() {
        lifecycleScope.launch {
            db.storyDao().getaddStories().collectLatest { stories ->
                adapter.updateData(stories)
            }
        }
    }

    private fun deleteStory(story: Story) {
        lifecycleScope.launch {
            db.storyDao().deleteStory(story.id)
            Toast.makeText(this@quanlytruyen, "Xóa truyện thành công", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToThemChuong(story: Story) {
        Log.d("DEBUG", "Chuyển đến trang thêm chương: ID=${story.id}")
        val intent = Intent(this, ThemChuong::class.java).apply {
            putExtra("storyId", story.id)
            putExtra("storyTitle", story.title)
            putExtra("storyCover", story.coverImage)
        }
        startActivity(intent)
    }

    private fun goToThemTruyen(story: Story) {
        Log.d("DEBUG", "Chuyển đến trang chỉnh sửa truyện: ID=${story.id}")
        val intent = Intent(this, themtruyen::class.java).apply {
            putExtra("storyId", story.id) // Truyền ID truyện để chỉnh sửa
            putExtra("storyTitle", story.title) // Truyền tên truyện
            putExtra("storyCover", story.coverImage) // Truyền ảnh bìa
        }
        startActivity(intent)
    }
}




