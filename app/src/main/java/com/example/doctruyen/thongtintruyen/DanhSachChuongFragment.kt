package com.example.doctruyen.thongtintruyen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.doctruyen.ReadStory
import com.example.doctruyen.entity.Chapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DanhSachChuongFragment : Fragment() {
    private var truyenId: Int = -1
    private var userId: Int = -1
    private var storyTitle: String = ""

    private lateinit var chuongAdapter: ChuongAdapter
    private lateinit var database: AppDatabase
    private lateinit var tvTongSoChuong: TextView
    private lateinit var btnSapXep: ImageButton
    private var danhSachChuong: MutableList<Chapter> = mutableListOf()
    private var isAscending = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            truyenId = it.getInt("TRUYEN_ID", -1)
            userId = it.getInt("USER_ID", -1)
            storyTitle = it.getString("STORY_TITLE", "") ?: ""
            Log.d("DanhSachChuong", "Đã nhận: TRUYEN_ID = $truyenId, USER_ID = $userId, STORY_TITLE = $storyTitle")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_danh_sach_chuong, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvDanhSachChuong)
        tvTongSoChuong = view.findViewById(R.id.tvTongSoChuong)
        btnSapXep = view.findViewById(R.id.btnSapXep)

        // Adapter khởi tạo rỗng ban đầu
        chuongAdapter = ChuongAdapter(emptyList()) { chapter ->
            val intent = Intent(requireContext(), ReadStory::class.java)
            intent.putExtra("CHUONG_ID", chapter.id)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("STORY_ID", truyenId)
            intent.putExtra("STORY_TITLE", storyTitle)
            Log.d("DanhSachChuong", "Truyền sang ReadStory: CHUONG_ID = ${chapter.id}, USER_ID = $userId, STORY_ID = $truyenId, STORY_TITLE = $storyTitle")
            startActivity(intent)

            // Tăng lượt xem
            lifecycleScope.launch(Dispatchers.IO) {
                val viewDao = database.viewDao()
                val currentView = viewDao.getUserStoryByStoryId(truyenId)

                if (currentView.isNullOrEmpty()) {
                    val newView = com.example.doctruyen.entity.View(story_id = truyenId, views = 1)
                    viewDao.insertUserStory(newView)
                } else {
                    viewDao.incrementViews(truyenId)
                }
            }
        }

        recyclerView.adapter = chuongAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        database = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "doctruyen1")
            .fallbackToDestructiveMigration()
            .build()

        loadChuongList()

        btnSapXep.setOnClickListener {
            isAscending = !isAscending
            capNhatDanhSach()
        }

        return view
    }

    private fun loadChuongList() {
        if (truyenId == -1) return

        lifecycleScope.launch(Dispatchers.IO) {
            danhSachChuong = database.chapterDao().getChaptersByStoryId(truyenId).toMutableList()

            withContext(Dispatchers.Main) {
                tvTongSoChuong.text = "Số chương (${danhSachChuong.size})"
                capNhatDanhSach()
            }
        }
    }

    private fun capNhatDanhSach() {
        danhSachChuong = if (isAscending) {
            danhSachChuong.sortedBy { it.id }.toMutableList()
        } else {
            danhSachChuong.sortedByDescending { it.id }.toMutableList()
        }

        chuongAdapter.updateData(danhSachChuong)
        btnSapXep.setImageResource(if (isAscending) R.drawable.sapxep else R.drawable.sapxep)
    }

    companion object {
        fun newInstance(truyenId: Int, userId: Int, storyTitle: String) = DanhSachChuongFragment().apply {
            arguments = Bundle().apply {
                putInt("TRUYEN_ID", truyenId)
                putInt("USER_ID", userId)
                putString("STORY_TITLE", storyTitle)
            }
        }
    }
}
