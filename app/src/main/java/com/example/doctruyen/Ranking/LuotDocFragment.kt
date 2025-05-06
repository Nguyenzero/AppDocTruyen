package com.example.doctruyen.Ranking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.doctruyen.R
import com.example.doctruyen.dao.StoryDao
import com.example.doctruyen.database.AppDatabase
import androidx.lifecycle.lifecycleScope
import com.example.doctruyen.dao.ViewDao
import kotlinx.coroutines.flow.first


import kotlinx.coroutines.launch

class LuotDocFragment : Fragment() {

    private lateinit var rankingDao: ViewDao
    private lateinit var storyDao: StoryDao
    private lateinit var adapter: RankingAdapter
    private lateinit var rvDanhSachTruyen: RecyclerView
    private var userId: Int = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_luot_doc, container, false)
        rvDanhSachTruyen = binding.findViewById(R.id.rvDanhSachTruyen)

        // Lấy dữ liệu từ DAO
        rankingDao = AppDatabase.getDatabase(requireContext()).viewDao()
        storyDao = AppDatabase.getDatabase(requireContext()).storyDao()

        userId = activity?.intent?.getIntExtra("USER_ID", -1) ?: -1


        val danhGiaDao = AppDatabase.getDatabase(requireContext()).danhGiaDao()


        // Quan sát LiveData để nhận giá trị khi có thay đổi
        rankingDao.getAllRankings().observe(viewLifecycleOwner, Observer { rankings ->
            // Log số lượng xếp hạng đã nhận được
            Log.d("Xếp hạng", "Dữ liệu xếp hạng đã được lấy: ${rankings.size} bản ghi")

            lifecycleScope.launch {

                val stories = storyDao.getAllStories().first()

                Log.d("Truyện", "Dữ liệu truyện đã được lấy: ${stories.size} bản ghi")

                // Cập nhật adapter
                adapter = RankingAdapter(requireContext(), rankings, stories,danhGiaDao,userId)
                rvDanhSachTruyen.layoutManager = LinearLayoutManager(requireContext())
                rvDanhSachTruyen.adapter = adapter
            }

        })

        return binding
    }
}









