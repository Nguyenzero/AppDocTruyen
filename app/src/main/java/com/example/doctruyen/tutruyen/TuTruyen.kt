package com.example.doctruyen.tutruyen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R
import com.example.doctruyen.Ranking.Ranking
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.doctruyen.ReadStory
import com.example.doctruyen.khampha.Khampha
import com.example.doctruyen.taikhoan.taikhoan
import com.google.android.material.bottomnavigation.BottomNavigationView

class TuTruyen : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var storyAdapter: StoryAdapter
    private var userId: Int = -1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tutruyen)


        userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            Log.e("TuTruyen", "Không có userId, cần quay về màn hình đăng nhập")
            finish() // hoặc chuyển về LoginActivity
            return
        }


        val edtSearch = findViewById<EditText>(R.id.edtSearch)
        val btnSearch = findViewById<ImageView>(R.id.btnSearch)
        recyclerView = findViewById(R.id.recyclerView)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val rootView = findViewById<View>(R.id.tutruyen1)
        val factory = TuTruyenViewModelFactory(application, userId)
        val tuTruyenViewModel: TuTruyenViewModel by viewModels { factory }


        rootView.setOnApplyWindowInsetsListener { v, insets ->
            v.setPadding(0, insets.systemWindowInsetTop, 0, 0)
            insets
        }
        storyAdapter = StoryAdapter(mutableListOf(), onItemClick = { story ->
            val intent = Intent(this, ReadStory::class.java).apply {
                putExtra("CHUONG_ID", story.id) // bạn cần lấy chương đầu tiên
                putExtra("STORY_ID", story.id) // ✅ thêm dòng này
                putExtra("FROM_TU_TRUYEN", true)
                putExtra("USER_ID", userId)
            }
            startActivity(intent)
        }, database = AppDatabase.getDatabase(this), userId = userId)


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = storyAdapter

        tuTruyenViewModel.bookmarkedStories.observe(this) { stories ->
            if (stories.isEmpty()) {
                Log.d("TuTruyen", "Không có truyện nào trong tủ truyện.")
            }
            storyAdapter.updateData(stories)
        }


        btnSearch.setOnClickListener {
            edtSearch.hint = "Tìm kiếm..."
        }
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_kham_pha -> {
                    val intent = Intent(this, Khampha::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    true
                }
                R.id.nav_xep_hang -> {
                    val intent = Intent(this, Ranking::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    true
                }
                R.id.nav_tai_khoan -> {
                    val intent = Intent(this, taikhoan::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }
}
