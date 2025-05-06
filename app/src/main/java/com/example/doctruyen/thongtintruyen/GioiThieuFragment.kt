package com.example.doctruyen.thongtintruyen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GioiThieuFragment : Fragment() {

    private var storyId: Int = -1
    private var description: String? = null

    private lateinit var tvSoChuong: TextView
    private lateinit var tvTinhTrang: TextView
    private lateinit var tvLuotDoc: TextView
    private lateinit var tvGioiThieu: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            storyId = it.getInt("STORY_ID")
            description = it.getString("DESCRIPTION")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gioi_thieu, container, false)

        tvSoChuong = view.findViewById(R.id.tvSoChuong)
        tvTinhTrang = view.findViewById(R.id.tvTinhTrang)
        tvLuotDoc = view.findViewById(R.id.tvLuotDoc)
        tvGioiThieu = view.findViewById(R.id.tvGioiThieu)

        tvGioiThieu.text = description ?: "Chưa có mô tả"

        loadData()

        return view
    }

    private fun loadData() {
        val database = AppDatabase.getDatabase(requireContext())

        lifecycleScope.launch {
            // Lấy tổng số chương
            val soChuong = withContext(Dispatchers.IO) {
                database.chapterDao().getChapterCountByStoryId(storyId)
            }
            tvSoChuong.text = soChuong.toString()

            // Lấy tình trạng truyện
            val story = withContext(Dispatchers.IO) {
                database.storyDao().getStoryById(storyId)
            }
            tvTinhTrang.text = story?.status ?: "Chưa rõ"

            // Lấy lượt đọc
            val luotDoc = withContext(Dispatchers.IO) {
                database.viewDao().getUserStoryByStoryId(storyId)?.firstOrNull()?.views ?: 0
            }
            tvLuotDoc.text = formatLuotDoc(luotDoc)
        }
    }

    private fun formatLuotDoc(views: Int): String {
        return when {
            views >= 1_000_000 -> String.format("%.2fM", views / 1_000_000f)
            views >= 1_000 -> String.format("%.2fK", views / 1_000f)
            else -> views.toString()
        }
    }

    companion object {
        fun newInstance(description: String, storyId: Int): GioiThieuFragment {
            val fragment = GioiThieuFragment()
            val args = Bundle()
            args.putString("DESCRIPTION", description)
            args.putInt("STORY_ID", storyId)
            fragment.arguments = args
            return fragment
        }
    }
}
