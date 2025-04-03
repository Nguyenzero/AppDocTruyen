    package com.example.doctruyen.khampha

    import android.content.Intent
    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import android.view.View
    import android.widget.Button
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.lifecycleScope
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.bumptech.glide.Glide
    import com.example.doctruyen.R
    import com.example.doctruyen.adapter.MoiNhatAdapter
    import com.example.doctruyen.adapter.OnTruyenMoiClickListener
    import com.example.doctruyen.adapter.StoryAdapterKhamPha
    import com.example.doctruyen.database.AppDatabase
    import com.example.doctruyen.entity.Story
    import com.example.doctruyen.taikhoan.taikhoan
    import com.example.doctruyen.thongtintruyen.ChiTietTruyen
    import com.example.doctruyen.tutruyen.TuTruyen
    import com.google.android.material.bottomnavigation.BottomNavigationView
    import kotlinx.coroutines.flow.collectLatest
    import kotlinx.coroutines.launch

    class Khampha : AppCompatActivity() {

        private lateinit var imgTruyen: ImageView
        private lateinit var tvTenTruyen: TextView
        private lateinit var tvTheLoai: TextView
        private lateinit var tvMoTa: TextView

        private lateinit var recyclerViewTruyen: RecyclerView
        private lateinit var storyAdapter: StoryAdapterKhamPha

        private lateinit var recyclerViewMoiNhat: RecyclerView
        private lateinit var moiNhatAdapter: MoiNhatAdapter
        private var currentStory: Story? = null



        private val handler = Handler(Looper.getMainLooper())

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.khampha)

            val btnDoc = findViewById<Button>(R.id.btnDoc)

            btnDoc.setOnClickListener {
                currentStory?.let { story ->
                    val intent = Intent(this, ChiTietTruyen::class.java).apply {
                        putExtra("TRUYEN_ID", story.id)
                        putExtra("TEN_TRUYEN", story.title ?: "Không có tên")
                        putExtra("TAC_GIA", story.author ?: "Không rõ tác giả")
                        putExtra("THE_LOAI", story.genre ?: "Chưa có thể loại")
                        putExtra("HINH_ANH", story.coverImage ?: "")
                        putExtra("DESCRIPTION", story.description)
                    }
                    startActivity(intent)
                }
            }




            val rootView = findViewById<View>(R.id.rootView)


            rootView.setOnApplyWindowInsetsListener { v, insets ->
                v.setPadding(0, insets.systemWindowInsetTop, 0, 0) // Đẩy nội dung xuống dưới
                insets
            }

            // Ánh xạ View
            imgTruyen = findViewById(R.id.imgTruyen)
            tvTenTruyen = findViewById(R.id.tvTenTruyen)
            tvTheLoai = findViewById(R.id.tvTheLoai)
            tvMoTa = findViewById(R.id.tvMoTa)

            recyclerViewTruyen = findViewById(R.id.recyclerViewTruyen)
            recyclerViewMoiNhat = findViewById(R.id.recyclerViewMoiNhat)

            // Khởi tạo adapter và gán vào RecyclerView mới nhất
            moiNhatAdapter = MoiNhatAdapter(emptyList(), object : OnTruyenMoiClickListener {
                override fun onTruyenMoiClick(truyen: Story) {
                    hienThiChiTietTruyen(truyen)
                }
            })
            recyclerViewMoiNhat.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewMoiNhat.adapter = moiNhatAdapter

            // Cấu hình RecyclerView danh sách truyện
            storyAdapter = StoryAdapterKhamPha(emptyList())
            recyclerViewTruyen.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewTruyen.adapter = storyAdapter

            // Thiết lập Bottom Navigation
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_tu_truyen -> {
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
            fetchStories()
            fetchRandomBannerImages()
        }

        private fun fetchLatestStory() {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                db.storyDao().getLatestStory().collectLatest { story ->
                    bindStoryData(story)
                    currentStory = story  // GÁN LUÔN VÀO currentStory
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
        }

        private fun fetchStories() {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                db.storyDao().getAllStories().collectLatest { stories ->
                    storyAdapter.updateData(stories)
                }
            }
        }


        private fun fetchRandomBannerImages() {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                db.storyDao().getAllStoriesNewestFirst().collectLatest { stories ->
                    moiNhatAdapter.updateData(stories)
                }
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            handler.removeCallbacksAndMessages(null)
        }

        private fun hienThiChiTietTruyen(truyen: Story) {
            currentStory = truyen  // Gán vào biến toàn cục
            Glide.with(this).load(truyen.coverImage).into(imgTruyen)
            tvTenTruyen.text = truyen.title
            tvTheLoai.text = truyen.genre
            tvMoTa.text = truyen.description
        }

    }
