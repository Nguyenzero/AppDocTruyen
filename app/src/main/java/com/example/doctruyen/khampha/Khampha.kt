    package com.example.doctruyen.khampha
    
    import android.content.Intent
    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import android.widget.ImageView
    import android.widget.RatingBar
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.lifecycleScope
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.LinearSnapHelper
    import androidx.recyclerview.widget.RecyclerView
    import com.bumptech.glide.Glide
    import com.example.doctruyen.R
    import com.example.doctruyen.adapter.BannerAdapter
    import com.example.doctruyen.adapter.StoryAdapterKhamPha
    import com.example.doctruyen.database.AppDatabase
    import com.example.doctruyen.entity.Story
    import com.example.doctruyen.taikhoan.taikhoan
    import com.example.doctruyen.tutruyen.TuTruyen
    import com.google.android.material.bottomnavigation.BottomNavigationView
    import kotlinx.coroutines.flow.collectLatest
    import kotlinx.coroutines.launch
    
    class Khampha : AppCompatActivity() {
    
        private lateinit var imgTruyen: ImageView
        private lateinit var tvTenTruyen: TextView
        private lateinit var tvTheLoai: TextView
        private lateinit var tvMoTa: TextView
        private lateinit var ratingBar: RatingBar
        private lateinit var recyclerViewTruyen: RecyclerView
        private lateinit var storyAdapter: StoryAdapterKhamPha
        private lateinit var recyclerViewBanner: RecyclerView
        private lateinit var bannerAdapter: BannerAdapter
        private val handler = Handler(Looper.getMainLooper())
        private var currentPage = 0
        private val delay: Long = 3000 // 3 giây
    
    
    
    
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.khampha)
    
            // Ánh xạ View
            imgTruyen = findViewById(R.id.imgTruyen)
            tvTenTruyen = findViewById(R.id.tvTenTruyen)
            tvTheLoai = findViewById(R.id.tvTheLoai)
            tvMoTa = findViewById(R.id.tvMoTa)
            ratingBar = findViewById(R.id.ratingBar)
    
            recyclerViewTruyen = findViewById(R.id.recyclerViewTruyen)
            storyAdapter = StoryAdapterKhamPha(emptyList())
            recyclerViewTruyen.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewTruyen.adapter = storyAdapter
    
            recyclerViewBanner = findViewById(R.id.recyclerViewBanner)
            bannerAdapter = BannerAdapter(emptyList())
    
            recyclerViewBanner.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewBanner.adapter = bannerAdapter
    
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(recyclerViewBanner)
    
            // Thiết lập Bottom Navigation
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_kham_pha -> {
                        startActivity(Intent(this, TuTruyen::class.java))
                        true
                    }
                    R.id.nav_xep_hang -> {
    
                        true
                    }
                    R.id.nav_tai_khoan -> {
                        startActivity(Intent(this, taikhoan::class.java))
                        true
                    }
                    else -> false
                }
            }
    
            // Gọi hàm để tải dữ liệu từ Room Database
            fetchLatestStory()
            fetchStories() // **Thêm dòng này để cập nhật RecyclerView**
            fetchBannerImages()  // 🔥 Thêm dòng này
            autoScrollBanner()
        }
    
        // Lấy truyện mới nhất và hiển thị ở phần đầu
        private fun fetchLatestStory() {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                db.storyDao().getLatestStory().collectLatest { story ->
                    bindStoryData(story)
                }
            }
        }
    
        private fun bindStoryData(story: Story) {
            Glide.with(this)
                .load(story.coverImage)
                .placeholder(R.drawable.kham_pha)
                .error(R.drawable.kham_pha)
                .into(imgTruyen)
    
            tvTenTruyen.text = story.title
            tvTheLoai.text = story.genre
            tvMoTa.text = story.description
            ratingBar.rating = 4.5f // Tạm thời đặt giá trị cố định
        }
    
        // Lấy danh sách truyện từ Room Database để cập nhật RecyclerView
        private fun fetchStories() {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                db.storyDao().getAllStories().collectLatest { stories ->
                    storyAdapter.updateData(stories)
                }
            }
        }
        private fun fetchBannerImages() {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                db.storyDao().getAllBannerImages().collectLatest { images ->
                    bannerAdapter.updateData(images)
                }
            }
        }
    
        private fun autoScrollBanner() {
            val runnable = object : Runnable {
                override fun run() {
                    if (bannerAdapter.itemCount > 0) {
                        currentPage = (currentPage + 1) % bannerAdapter.itemCount
                        recyclerViewBanner.smoothScrollToPosition(currentPage)
                        handler.postDelayed(this, delay)
                    }
                }
            }
            handler.postDelayed(runnable, delay)
        }
    
        override fun onDestroy() {
            super.onDestroy()
            handler.removeCallbacksAndMessages(null)
        }
    }
