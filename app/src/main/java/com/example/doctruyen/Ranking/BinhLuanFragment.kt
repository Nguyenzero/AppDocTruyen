package com.example.doctruyen.Ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.Story
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class BinhLuanFragment : Fragment() {

    private lateinit var rvDanhSachTruyen: RecyclerView
    private lateinit var adapter: BinhLuanAdapter
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.xephang_binhluan, container, false)
        rvDanhSachTruyen = view.findViewById(R.id.rvDanhSachTruyenbinhluan)
        rvDanhSachTruyen.layoutManager = LinearLayoutManager(requireContext())

        val db = AppDatabase.getDatabase(requireContext())
        val danhGiaDao = db.danhGiaDao()
        val storyDao = db.storyDao()

        userId = activity?.intent?.getIntExtra("USER_ID", -1) ?: -1

        lifecycleScope.launch {
            // combine để lắng nghe cả hai bảng cùng lúc
            combine(
                danhGiaDao.getAllDanhGia(),
                storyDao.getAllStories()
            ) { danhGiaList, storyList ->

                val groupedByStory = danhGiaList.groupBy { it.story_id }

                val resultList = storyList.map { story ->
                    val danhGias = groupedByStory[story.id] ?: emptyList()
                    val avgRating = if (danhGias.isNotEmpty()) {
                        danhGias.map { it.rank }.average().toFloat()
                    } else 0f
                    val commentCount = danhGias.size
                    Triple(story, avgRating, commentCount)
                }

                // Sắp xếp theo số sao rồi số bình luận
                resultList.sortedWith(
                    compareByDescending<Triple<Story, Float, Int>> { it.second }
                        .thenByDescending { it.third }
                )
            }.collect { sortedList ->
                adapter = BinhLuanAdapter(requireContext(), sortedList, userId)
                rvDanhSachTruyen.adapter = adapter
            }
        }

        return view
    }
}
