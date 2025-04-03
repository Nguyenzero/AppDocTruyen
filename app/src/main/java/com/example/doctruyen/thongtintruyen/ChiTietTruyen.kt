package com.example.doctruyen.thongtintruyen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.doctruyen.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import androidx.appcompat.widget.Toolbar


class ChiTietTruyen : AppCompatActivity() {
    private var truyenId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thong_tin_truyen)


        truyenId = intent.getIntExtra("TRUYEN_ID", -1)

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

        btnBack.setOnClickListener {
            finish()
        }

        // Nhận dữ liệu từ Intent
        val tenTruyen = intent.getStringExtra("TEN_TRUYEN") ?: "Không có tên"
        val tacGia = intent.getStringExtra("TAC_GIA") ?: "Không rõ tác giả"
        val theLoai = intent.getStringExtra("THE_LOAI") ?: "Chưa có thể loại"
        val hinhAnh = intent.getStringExtra("HINH_ANH") ?: ""
        val description = intent.getStringExtra("DESCRIPTION") ?: "Chưa có mô tả"
        val rating = 4.5f // Giá trị tạm thời, bạn có thể thay bằng dữ liệu từ CSDL

        // Gán dữ liệu lên giao diện
        txtTenTruyen.text = tenTruyen
        txtTacGia.text = "Tác giả: $tacGia"
        txtTheLoai.text = "Thể loại: $theLoai"
        txtRating.text = rating.toString()

        // Load ảnh từ URL
        Glide.with(this).load(hinhAnh).into(imgBanner)
        Glide.with(this).load(hinhAnh).into(imgBookCover)


        val adapter = TabTrang(this, description, truyenId)
        viewPager.adapter = adapter
        Log.d("ChiTietTruyen", "Truyen ID truyền vào Fragment: $truyenId")

        val tabTitles = listOf("Giới Thiệu", "Đánh Giá", "D.S Chương")
        viewPager.offscreenPageLimit = tabTitles.size

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}
