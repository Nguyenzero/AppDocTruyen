package com.example.doctruyen.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R

class DanhSachTruyenDanhGia : AppCompatActivity() {
    private lateinit var viewModel: TruyenDanhGiaViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TruyenDanhGiaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quan_ly_danh_gia)

        recyclerView = findViewById(R.id.recyclerViewDanhSachTruyen)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TruyenDanhGiaAdapter(emptyList())
        recyclerView.adapter = adapter

        // Xử lý sự kiện khi nhấn vào item
        adapter.setOnItemClickListener { story ->
            val intent = Intent(this, ChiTietDanhGiaActivity::class.java)
            intent.putExtra("story_id", story.id)
            startActivity(intent)
        }

        viewModel = ViewModelProvider(this).get(TruyenDanhGiaViewModel::class.java)

        viewModel.listStoryWithReview.observe(this) { list ->
            adapter.updateList(list)
        }

        viewModel.loadStoriesWithReviewCount()
    }
}
