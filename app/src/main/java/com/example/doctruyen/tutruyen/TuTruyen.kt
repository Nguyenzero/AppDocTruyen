package com.example.doctruyen.tutruyen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.doctruyen.ReadStory
import com.example.doctruyen.entity.Story
import com.example.doctruyen.khampha.Khampha
import com.example.doctruyen.taikhoan.taikhoan
import com.google.android.material.bottomnavigation.BottomNavigationView

    class TuTruyen : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var storyAdapter: StoryAdapter
    private val userId = 1 // Giả sử ID người dùng là 1


    private val tuTruyenViewModel: TuTruyenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tutruyen)

        val edtSearch = findViewById<EditText>(R.id.edtSearch)
        val btnSearch = findViewById<ImageView>(R.id.btnSearch)
        recyclerView = findViewById(R.id.recyclerView)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val rootView = findViewById<View>(R.id.tutruyen1)


        rootView.setOnApplyWindowInsetsListener { v, insets ->
            v.setPadding(0, insets.systemWindowInsetTop, 0, 0)
            insets
        }

        storyAdapter = StoryAdapter(mutableListOf(), onItemClick = { story ->
            val intent = Intent(this, ReadStory::class.java).apply {
                putExtra("CHUONG_ID", story.id)
                putExtra("FROM_TU_TRUYEN", true)
            }
            startActivity(intent)
        }, database = AppDatabase.getDatabase(this), userId = userId)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = storyAdapter

        // Quan sát dữ liệu từ ViewModel
        tuTruyenViewModel.bookmarkedStories.observe(this,  { stories ->
            storyAdapter.updateData(stories)
        })

        // Xử lý sự kiện tìm kiếm
        btnSearch.setOnClickListener {
            edtSearch.hint = "Tìm kiếm..."
        }

        // Xử lý sự kiện BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_kham_pha -> {
                    startActivity(Intent(this, Khampha::class.java))
                    true
                }
                R.id.nav_xep_hang -> {
                    // startActivity(Intent(this, XepHang::class.java))
                    true
                }
                R.id.nav_tai_khoan -> {
                    startActivity(Intent(this, taikhoan::class.java))
                    true
                }
                else -> false
            }
        }


    }


    }
