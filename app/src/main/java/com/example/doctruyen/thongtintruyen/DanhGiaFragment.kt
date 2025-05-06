package com.example.doctruyen.thongtintruyen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R

import com.example.doctruyen.database.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.doctruyen.entity.DanhGia
import com.example.doctruyen.thongtintruyen.DanhGiaMain

class DanhGiaFragment : Fragment() {

    private var truyenId: Int = -1
    private var userId: Int = -1

    private lateinit var recyclerView: RecyclerView
    private lateinit var danhGiaAdapter: DanhGiaAdapter
    private lateinit var tvSoDanhGia: TextView
    private lateinit var btnMoi: Button
    private lateinit var btnCu: Button

    private var isNew = true  // Biến kiểm tra xem người dùng muốn xem đánh giá mới hay cũ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            truyenId = it.getInt("TRUYEN_ID", -1)
            userId = it.getInt("USER_ID", -1)
        }
        Log.d("DanhGiaFragment", "userId nhan: $userId, truyenId received: $truyenId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_danh_gia, container, false)

        recyclerView = view.findViewById(R.id.recyclerDanhGia)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        tvSoDanhGia = view.findViewById(R.id.tvSoDanhGia)
        btnMoi = view.findViewById(R.id.btnMoi)
        btnCu = view.findViewById(R.id.btnCu)


        val btnThemDanhGia = view.findViewById<FloatingActionButton>(R.id.btnThemDanhGia)
        btnThemDanhGia.setOnClickListener {
            val intent = Intent(requireContext(), DanhGiaMain::class.java)
            intent.putExtra("storyId", truyenId)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        btnMoi.setOnClickListener {
            // Cập nhật dữ liệu khi chọn "Mới"
            isNew = true
            loadDanhGia()
            // Thay đổi trạng thái nút
            btnMoi.setBackgroundColor(resources.getColor(R.color.black))
            btnCu.setBackgroundColor(resources.getColor(R.color.grey))
        }

        btnCu.setOnClickListener {
            // Cập nhật dữ liệu khi chọn "Cũ"
            isNew = false
            loadDanhGia()
            // Thay đổi trạng thái nút
            btnCu.setBackgroundColor(resources.getColor(R.color.black))
            btnMoi.setBackgroundColor(resources.getColor(R.color.grey))
        }

        // Tải dữ liệu ban đầu (dữ liệu mới)
        loadDanhGia()

        return view
    }

    private fun loadDanhGia() {
        val db = AppDatabase.getDatabase(requireContext())

        lifecycleScope.launch(Dispatchers.IO) {
            val danhSach: List<DanhGia> = if (isNew) {
                // Lấy danh sách đánh giá mới nhất
                db.danhGiaDao().getDanhGiaTheoTruyenMoi(truyenId)
            } else {
                // Lấy danh sách đánh giá cũ nhất
                db.danhGiaDao().getDanhGiaTheoTruyenCu(truyenId)
            }

            // Cập nhật RecyclerView
            withContext(Dispatchers.Main) {
                danhGiaAdapter = DanhGiaAdapter(danhSach, requireContext())
                recyclerView.adapter = danhGiaAdapter

                // Cập nhật số lượng đánh giá
                tvSoDanhGia.text = "Đánh giá (${danhSach.size})"
            }
        }
    }

    companion object {
        fun newInstance(truyenId: Int, userId: Int): DanhGiaFragment {
            val fragment = DanhGiaFragment()
            val args = Bundle()
            args.putInt("TRUYEN_ID", truyenId)
            args.putInt("USER_ID", userId)
            fragment.arguments = args
            return fragment
        }
    }
}



