package com.example.doctruyen.tutruyen

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R
import com.example.doctruyen.khampha
import com.example.doctruyen.taikhoan.taikhoan
import com.google.android.material.bottomnavigation.BottomNavigationView

class TuTruyen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tutruyen)

        val edtSearch = findViewById<EditText>(R.id.edtSearch)
        val btnSearch = findViewById<ImageView>(R.id.btnSearch)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Xử lý sự kiện tìm kiếm
        btnSearch.setOnClickListener {
            edtSearch.hint = "Tìm kiếm..."
        }

        // Dữ liệu danh sách truyện giả lập
        val storyList = listOf(
            Story("One Piece", "Hành động", R.drawable.khampha),
            Story("Naruto", "Phiêu lưu", R.drawable.tutruyen),
            Story("Attack on Titan", "Kịch tính", R.drawable.xephang),
            Story("Doraemon", "Hài hước", R.drawable.taikhoan)
        )

        // Cấu hình RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StoryAdapter(storyList)

        // Xử lý sự kiện click của BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_kham_pha -> {
                    val intent = Intent(this, khampha::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_xep_hang -> {
                    // val intent = Intent(this, XepHang::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_tai_khoan -> {
                     val intent = Intent(this, taikhoan::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}
