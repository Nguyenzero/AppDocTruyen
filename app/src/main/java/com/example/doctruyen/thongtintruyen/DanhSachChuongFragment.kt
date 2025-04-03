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
    private lateinit var chuongAdapter: ChuongAdapter
    private lateinit var database: AppDatabase
    private lateinit var tvTongSoChuong: TextView
    private lateinit var btnSapXep: ImageButton
    private var danhSachChuong: MutableList<Chapter> = mutableListOf()
    private var isAscending = true // Mặc định sắp xếp từ A-Z

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            truyenId = it.getInt("TRUYEN_ID", -1)
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

        // Khởi tạo adapter với danh sách rỗng
        chuongAdapter = ChuongAdapter(emptyList()) { chapter ->
            Log.d("DanhSachChuongFragment", "Chương được chọn: ID = ${chapter.id}") // In log để kiểm tra ID
            val intent = Intent(requireContext(), ReadStory::class.java)
            intent.putExtra("CHUONG_ID", chapter.id)
            startActivity(intent)
        }
        recyclerView.adapter = chuongAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        database = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "doctruyen_db")
            .fallbackToDestructiveMigration()
            .build()

        loadChuongList()

        // Xử lý sự kiện bấm nút sắp xếp
        btnSapXep.setOnClickListener {
            isAscending = !isAscending // Đảo trạng thái sắp xếp
            capNhatDanhSach() // Sắp xếp lại danh sách
        }

        return view
    }

    private fun loadChuongList() {
        if (truyenId == -1) return

        lifecycleScope.launch(Dispatchers.IO) {
            danhSachChuong = database.chapterDao().getChaptersByStoryId(truyenId).toMutableList()

            withContext(Dispatchers.Main) {
                // Hiển thị tổng số chương
                tvTongSoChuong.text = "Số chương (${danhSachChuong.size})"
                capNhatDanhSach() // Hiển thị danh sách theo thứ tự mặc định (A-Z)
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
        fun newInstance(truyenId: Int) = DanhSachChuongFragment().apply {
            arguments = Bundle().apply {
                putInt("TRUYEN_ID", truyenId)
            }
        }
    }
}
