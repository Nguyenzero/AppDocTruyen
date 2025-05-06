package com.example.doctruyen.admin

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.DanhGia
import kotlinx.coroutines.launch
import java.util.*

class ChiTietDanhGiaActivity : AppCompatActivity() {

    private lateinit var tenTruyen: TextView
    private lateinit var anhBia: ImageView
    private lateinit var soDanhGia: TextView
    private lateinit var danhGiaRecyclerView: RecyclerView
    private lateinit var adapter: DanhGiaAdapter
    private var storyId: Int = -1
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quan_ly_danh_gia_chi_tiet)

        tenTruyen = findViewById(R.id.tvTenTruyen)
        anhBia = findViewById(R.id.imgAnhBiaTruyen)
        soDanhGia = findViewById(R.id.tvSoDanhGia)
        danhGiaRecyclerView = findViewById(R.id.recyclerViewDanhGiaTruyen)
        danhGiaRecyclerView.layoutManager = LinearLayoutManager(this)

        storyId = intent.getIntExtra("story_id", -1)
        db = AppDatabase.getDatabase(this)

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        loadDanhGia()
    }

    private fun loadDanhGia() {
        lifecycleScope.launch {
            val story = db.storyDao().getStoryById(storyId)
            val danhGias = db.danhGiaDao().getDanhGiaByStoryId(storyId)

            val userIds = danhGias.map { it.user_id }.distinct()
            val users = db.userDao().getUsersByIds(userIds)
            val userMap = users.associateBy { it.id }

            story?.let {
                tenTruyen.text = it.title
                Glide.with(this@ChiTietDanhGiaActivity)
                    .load(it.coverImage)
                    .placeholder(R.drawable.khampha)
                    .error(R.drawable.kham_pha)
                    .into(anhBia)

                soDanhGia.text = "Số lượng đánh giá: ${danhGias.size}"

                adapter = DanhGiaAdapter(
                    this@ChiTietDanhGiaActivity,
                    danhGias,
                    userMap
                ) { danhGia -> deleteDanhGia(danhGia) }

                danhGiaRecyclerView.adapter = adapter
            }
        }
    }

    private fun deleteDanhGia(danhGia: DanhGia) {
        lifecycleScope.launch {
            db.danhGiaDao().delete(danhGia)
            loadDanhGia() // Tải lại danh sách sau khi xóa
        }
    }
}
