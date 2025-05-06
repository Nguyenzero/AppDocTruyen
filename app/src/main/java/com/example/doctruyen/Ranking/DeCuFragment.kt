package com.example.doctruyen.Ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.DanhGia
import com.example.doctruyen.entity.Story
import com.example.doctruyen.entity.View as ViewEntity
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.math.log10

class DeCuFragment : Fragment() {

    private lateinit var rvDanhSachTruyen: RecyclerView
    private lateinit var adapter: DeCuAdapter
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_de_cu, container, false)
        rvDanhSachTruyen = view.findViewById(R.id.rvDanhSachTruyenDecu)
        rvDanhSachTruyen.layoutManager = LinearLayoutManager(requireContext())

        val db = AppDatabase.getDatabase(requireContext())
        val danhGiaDao = db.danhGiaDao()
        val storyDao = db.storyDao()
        val viewDao = db.viewDao()

        userId = activity?.intent?.getIntExtra("USER_ID", -1) ?: -1

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
                }.sortedByDescending { it.second }

            }.collect { resultList ->
                adapter = DeCuAdapter(requireContext(), resultList, userId)
                rvDanhSachTruyen.adapter = adapter
            }
        }

        return view
    }
}
