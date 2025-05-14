            package com.example.doctruyen.khampha

            import android.content.Intent
            import android.os.Bundle
            import android.os.Handler
            import android.os.Looper
            import android.view.View
            import android.widget.Button
            import android.widget.EditText
            import android.widget.ImageView
            import android.widget.TextView
            import androidx.appcompat.app.AppCompatActivity
            import androidx.core.content.ContentProviderCompat.requireContext
            import androidx.lifecycle.asFlow
            import androidx.lifecycle.lifecycleScope
            import androidx.recyclerview.widget.GridLayoutManager
            import androidx.recyclerview.widget.LinearLayoutManager
            import androidx.recyclerview.widget.RecyclerView
            import androidx.viewpager2.widget.ViewPager2
            import com.bumptech.glide.Glide
            import com.example.doctruyen.R
            import com.example.doctruyen.Ranking.Ranking
            import com.example.doctruyen.TimKiem.TimKiemActivity
            import com.example.doctruyen.adapter.MoiNhatAdapter
            import com.example.doctruyen.adapter.OnTruyenMoiClickListener
            import com.example.doctruyen.adapter.StoryAdapterKhamPha
            import com.example.doctruyen.database.AppDatabase
            import com.example.doctruyen.entity.Story
            import com.example.doctruyen.khampha.DeCuAdapterKhamPha
            import com.example.doctruyen.taikhoan.taikhoan
            import com.example.doctruyen.thongtintruyen.ChiTietTruyen
            import com.example.doctruyen.tutruyen.TuTruyen
            import com.google.android.material.bottomnavigation.BottomNavigationView
            import kotlinx.coroutines.CoroutineScope
            import kotlinx.coroutines.Dispatchers
            import kotlinx.coroutines.flow.collectLatest
            import kotlinx.coroutines.flow.combine
            import kotlinx.coroutines.launch
            import kotlinx.coroutines.withContext

            import kotlin.math.log10


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

                private var userId: Int = -1  // Lưu trữ ID người dùng


                private val handler = Handler(Looper.getMainLooper())

                private lateinit var txtRating: TextView

                private lateinit var recyclerViewDeCu: RecyclerView
                private lateinit var deCuAdapter: DeCuAdapterKhamPha

                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    setContentView(R.layout.khampha)

                    // Nhận userId từ Intent
                    userId = intent.getIntExtra("USER_ID", -1)

                    val btnDoc = findViewById<Button>(R.id.btnDoc)

                    btnDoc.setOnClickListener {
                        currentStory?.let { story ->
                            val intent = Intent(this, ChiTietTruyen::class.java).apply {
                                putExtra("TRUYEN_ID", story.id)
                                putExtra("USER_ID", userId) // Truyền userId vào ChiTietTruyen
                                putExtra("TEN_TRUYEN", story.title ?: "Không có tên")
                                putExtra("TAC_GIA", story.author ?: "Không rõ tác giả")
                                putExtra("THE_LOAI", story.genre ?: "Chưa có thể loại")
                                putExtra("HINH_ANH", story.coverImage ?: "")
                                putExtra("DESCRIPTION", story.description)
                                putExtra("STORY_ID", story.id) // dùng story.id thay vì biến storyId = -1

                            }
                            startActivity(intent)
                        }
                    }

                    val rootView = findViewById<View>(R.id.rootView)
                    rootView.setOnApplyWindowInsetsListener { v, insets ->
                        v.setPadding(0, insets.systemWindowInsetTop, 0, 0) // Đẩy nội dung xuống dưới
                        insets
                    }



                    val tenmoinhat = findViewById<ImageView>(R.id.tenmoinhat)
                    val tenTatCa = findViewById<ImageView>(R.id.tenTatCa)
                    val tenDeCu = findViewById<ImageView>(R.id.tendecu)

                    tenmoinhat.setOnClickListener {
                        val intent = Intent(this, TimKiemActivity::class.java)
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                    }
                    tenTatCa.setOnClickListener {
                        val intent = Intent(this, TimKiemActivity::class.java)
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                    }
                    tenDeCu.setOnClickListener {
                        val intent = Intent(this, TimKiemActivity::class.java)
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                    }


                    recyclerViewDeCu = findViewById(R.id.recyclerViewDeCu)
                    recyclerViewDeCu.layoutManager = GridLayoutManager(this, 3)


                    // Ánh xạ View
                    imgTruyen = findViewById(R.id.imgTruyen)
                    tvTenTruyen = findViewById(R.id.tvTenTruyen)
                    tvTheLoai = findViewById(R.id.tvTheLoai)
                    tvMoTa = findViewById(R.id.tvMoTa)

                    txtRating = findViewById(R.id.txtRating)


                    val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)
                    searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
                        if (hasFocus) {
                            val intent = Intent(this, TimKiemActivity::class.java)
                            intent.putExtra("USER_ID", userId)
                            startActivity(intent)
                            searchView.clearFocus() // Tránh giữ bàn phím
                        }
                    }


                    recyclerViewTruyen = findViewById(R.id.recyclerViewTruyen)
                    recyclerViewMoiNhat = findViewById(R.id.recyclerViewMoiNhat)

                    // Khởi tạo adapter và gán vào RecyclerView mới nhất
                    moiNhatAdapter = MoiNhatAdapter(emptyList(), object : OnTruyenMoiClickListener {
                        override fun onTruyenMoiClick(truyen: Story) {
                            hienThiChiTietTruyen(truyen)
                        }
                    })


                    val bannerImages = listOf(
                        R.drawable.app1,
                        R.drawable.app2
                    )

                    val adapter = BannerAdapter(bannerImages)
                    val viewPager = findViewById<ViewPager2>(R.id.bannerViewPager)

                    viewPager.adapter = adapter



                    viewPager.clipToPadding = false
                    viewPager.clipChildren = false
                    viewPager.offscreenPageLimit = 3
                    (viewPager.getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER


                    val bannerHandler = Handler(Looper.getMainLooper())
                    var currentPage = 0

                    val bannerRunnable = object : Runnable {
                        override fun run() {
                            if (currentPage == bannerImages.size) currentPage = 0
                            viewPager.setCurrentItem(currentPage++, true)
                            bannerHandler.postDelayed(this, 2000) // Chuyển sau 2 giây
                        }
                    }

                    bannerHandler.post(bannerRunnable)


                    recyclerViewMoiNhat.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    recyclerViewMoiNhat.adapter = moiNhatAdapter

                    val db = AppDatabase.getDatabase(applicationContext)
                    val danhGiaDao = db.danhGiaDao()

                    storyAdapter = StoryAdapterKhamPha(emptyList(), userId, danhGiaDao)
                    recyclerViewTruyen.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    recyclerViewTruyen.adapter = storyAdapter

                    // Thiết lập Bottom Navigation
                    val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                    bottomNavigationView.setOnItemSelectedListener { item ->
                        when (item.itemId) {
                            R.id.nav_tu_truyen -> {
                                val intent = Intent(this, TuTruyen::class.java)
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



                    // Gọi hàm để tải dữ liệu từ Room Database
                    fetchLatestStory()
                    fetchStories()
                    fetchRandomBannerImages()
                    fetchDeCuStories()

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

                    private fun fetchDeCuStories() {
                        val db = AppDatabase.getDatabase(applicationContext)
                        val storyDao = db.storyDao()
                        val danhGiaDao = db.danhGiaDao()
                        val viewDao = db.viewDao()

                        lifecycleScope.launch {
                            combine(
                                storyDao.getAllStories(),
                                danhGiaDao.getAllDanhGia(),
                                viewDao.getAllRankings().asFlow()
                            ) { stories, danhgias, views ->
                                stories.map { story ->
                                    val storyDanhgias = danhgias.filter { it.story_id == story.id }
                                    val avgRating = if (storyDanhgias.isNotEmpty()) {
                                        storyDanhgias.map { it.rank }.average()
                                    } else 0.0

                                    val viewCount = views.firstOrNull { it.story_id == story.id }?.views ?: 0
                                    val score = avgRating * 2 + log10(viewCount + 1.0)
                                    Triple(story, score, viewCount)
                                }.sortedByDescending { it.second } // Sắp xếp theo điểm
                            }.collect { resultList ->
                                val topStories = resultList.map { it.first }.take(6) // Lấy top 6 truyện
                                deCuAdapter = DeCuAdapterKhamPha(topStories,userId)
                                recyclerViewDeCu.adapter = deCuAdapter
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

                    // Quan sát điểm đánh giá (LiveData)
                    val db = AppDatabase.getDatabase(applicationContext)
                    db.danhGiaDao().getLiveAverageRatingByStoryId(story.id ?: -1).observe(this) { avgRating ->
                        val rating = avgRating ?: 0f
                        txtRating.text = String.format("%.1f", rating)
                    }
                }



                private fun fetchStories() {
                    lifecycleScope.launch {
                        val db = AppDatabase.getDatabase(applicationContext)
                        db.storyDao().getaddStories().collectLatest { stories ->
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

                    // Quan sát điểm đánh giá (LiveData)
                    val db = AppDatabase.getDatabase(applicationContext)
                    db.danhGiaDao().getLiveAverageRatingByStoryId(truyen.id ?: -1).observe(this) { avgRating ->
                        val rating = avgRating ?: 0f
                        txtRating.text = String.format("%.1f", rating)
                    }
                }



            }

