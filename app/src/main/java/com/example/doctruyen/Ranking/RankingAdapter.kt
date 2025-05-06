package com.example.doctruyen.Ranking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctruyen.R
import com.example.doctruyen.dao.DanhGiaDao
import com.example.doctruyen.entity.Story
import com.example.doctruyen.thongtintruyen.ChiTietTruyen
import kotlinx.coroutines.*

class RankingAdapter(
    private val context: Context,
    private val rankings: List<com.example.doctruyen.entity.View>,
    private val stories: List<Story>,
    private val danhGiaDao: DanhGiaDao,
    private var userId: Int = -1
) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ranking, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val viewData = rankings[position]
        val story = stories.find { it.id == viewData.story_id }

        if (story != null) {
            holder.tvTitle.text = story.title
            holder.tvCategory.text = story.genre
            holder.tvAuthor.text = story.author
            holder.tvViews.text = "Views: ${viewData.views}"
            Glide.with(context).load(story.coverImage).into(holder.ivStoryCover)

            // Quan sát LiveData từ Room
            danhGiaDao.getLiveAverageRatingByStoryId(story.id)
                .observeForever { avgRating ->
                    holder.tvRating.text = String.format("⭐ %.1f", avgRating ?: 0f)
                }

            holder.itemView.setOnClickListener {
                val intent = android.content.Intent(context, ChiTietTruyen::class.java).apply {
                    putExtra("TRUYEN_ID", story.id)
                    putExtra("USER_ID", userId)
                    putExtra("TEN_TRUYEN", story.title ?: "Không có tên")
                    putExtra("TAC_GIA", story.author ?: "Không rõ tác giả")
                    putExtra("THE_LOAI", story.genre ?: "Chưa có thể loại")
                    putExtra("HINH_ANH", story.coverImage ?: "")
                    putExtra("DESCRIPTION", story.description ?: "Chưa có mô tả")
                }
                context.startActivity(intent)
            }

        } else {
            holder.tvTitle.text = "Truyện không có sẵn"
            holder.tvCategory.text = "N/A"
            holder.tvAuthor.text = "N/A"
            holder.tvViews.text = "Views: ${viewData.views}"
            holder.tvRating.text = "⭐ 0.0"
        }
    }

    override fun getItemCount(): Int = rankings.size

    class RankingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvAuthor: TextView = view.findViewById(R.id.tvAuthor)
        val tvViews: TextView = view.findViewById(R.id.tvViews)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val ivStoryCover: ImageView = view.findViewById(R.id.imgCover)
    }
}
