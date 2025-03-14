package com.example.doctruyen.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R

import com.example.doctruyen.amdin.TruyenAdapter
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.Story
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest  // Thêm collectLatest để dùng với Flow

class quanlytruyen : AppCompatActivity() {

    private lateinit var recyclerTruyen: RecyclerView
    private lateinit var btnThemTruyen: Button
    private lateinit var adapter: TruyenAdapter
    private lateinit var db: AppDatabase
    private lateinit var btnBack: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quan_ly_truyen)

        recyclerTruyen = findViewById(R.id.recyclerTruyen)
        btnThemTruyen = findViewById(R.id.btnThemTruyen)
        btnBack = findViewById(R.id.btnBack)


        db = AppDatabase.getDatabase(this)

        adapter = TruyenAdapter(emptyList(), ::deleteStory)
        recyclerTruyen.layoutManager = LinearLayoutManager(this)
        recyclerTruyen.adapter = adapter


        btnBack.setOnClickListener {
            finish() // Đóng activity và quay lại trang trước
        }

        btnThemTruyen.setOnClickListener {
            val intent = Intent(this, themtruyen::class.java)
            startActivity(intent)
        }

        loadStories()
    }

    private fun loadStories() {
        lifecycleScope.launch {
            db.storyDao().getAllStories().collectLatest { stories ->
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
}
