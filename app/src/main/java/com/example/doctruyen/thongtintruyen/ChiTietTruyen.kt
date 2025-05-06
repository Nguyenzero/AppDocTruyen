package com.example.doctruyen.thongtintruyen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.doctruyen.ReadStory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import androidx.appcompat.widget.Toolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChiTietTruyen : AppCompatActivity() {
    private var truyenId: Int = -1
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thong_tin_truyen)

        truyenId = intent.getIntExtra("TRUYEN_ID", -1)
        userId = intent.getIntExtra("USER_ID", -1)

        Log.d("ChiTietTruyen", "userId nhận được: $userId, truyenId nhận được: $truyenId")

        // Ánh xạ view
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val imgBanner = findViewById<ImageView>(R.id.bannerImage)
        val imgBookCover = findViewById<ImageView>(R.id.bookCover)
        val txtTenTruyen = findViewById<TextView>(R.id.bookTitle)
        val txtTacGia = findViewById<TextView>(R.id.bookAuthor)
        val txtTheLoai = findViewById<TextView>(R.id.txtTheLoai)
        val txtRating = findViewById<TextView>(R.id.txtRating)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnRead = findViewById<Button>(R.id.btnRead)

        btnBack.setOnClickListener { finish() }

        btnRead.setOnClickListener {
            val db = AppDatabase.getDatabase(this)
            val bookmarkDao = db.bookmarkDao()

            CoroutineScope(Dispatchers.Main).launch {
                val lastChapterNumber = bookmarkDao.getLastReadChapterNumber(userId, truyenId)
                if (lastChapterNumber != null) {
                    openChapter(truyenId, lastChapterNumber)
                } else {
                    openChapter(truyenId, 1)
                }
            }
        }

        val tenTruyen = intent.getStringExtra("TEN_TRUYEN") ?: "Không có tên"
        val tacGia = intent.getStringExtra("TAC_GIA") ?: "Không rõ tác giả"
        val theLoai = intent.getStringExtra("THE_LOAI") ?: "Chưa có thể loại"
        val hinhAnh = intent.getStringExtra("HINH_ANH") ?: ""
        val description = intent.getStringExtra("DESCRIPTION") ?: "Chưa có mô tả"

        txtTenTruyen.text = tenTruyen
        txtTacGia.text = "Tác giả: $tacGia"
        txtTheLoai.text = "Thể loại: $theLoai"

        Glide.with(this).load(hinhAnh).into(imgBanner)
        Glide.with(this).load(hinhAnh).into(imgBookCover)

        val adapter = TabTrang(this, description, truyenId, userId,tenTruyen)

        viewPager.adapter = adapter
        val tabTitles = listOf("Giới Thiệu", "Đánh Giá", "D.S Chương")
        viewPager.offscreenPageLimit = tabTitles.size

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        val db = AppDatabase.getDatabase(this)
        val danhGiaDao = db.danhGiaDao()

        danhGiaDao.getLiveAverageRatingByStoryId(truyenId).observe(this) { avgRating ->
            val ratingText = String.format("%.1f", avgRating ?: 0f)
            txtRating.text = ratingText
        }
    }

    private fun openChapter(storyId: Int, chapterNumber: Int) {
        val intent = Intent(this, ReadStory::class.java)
        intent.putExtra("STORY_ID", storyId)
        intent.putExtra("CHUONG_ID", chapterNumber)  // CHỈ GỬI SỐ
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
    }
}
