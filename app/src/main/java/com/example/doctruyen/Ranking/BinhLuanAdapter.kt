package com.example.doctruyen.Ranking

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctruyen.R
import com.example.doctruyen.entity.Story
import com.example.doctruyen.thongtintruyen.ChiTietTruyen

class BinhLuanAdapter(
    private val context: Context,
    private val storyList: List<Triple<Story, Float, Int>>,
    private val userId: Int
) : RecyclerView.Adapter<BinhLuanAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvAuthor: TextView = view.findViewById(R.id.tvAuthor)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val ivStoryCover: ImageView = view.findViewById(R.id.imgCover)
        val tvViews: TextView = view.findViewById(R.id.tvViews)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ranking, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = storyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (story, avgRating, commentCount) = storyList[position]

        holder.tvTitle.text = story.title
        holder.tvAuthor.text = story.author
        holder.tvCategory.text = story.genre
        holder.tvRating.text = String.format("⭐ %.1f", avgRating)
        holder.tvViews.text = "💬 $commentCount"
        Glide.with(context).load(story.coverImage).into(holder.ivStoryCover)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChiTietTruyen::class.java).apply {
                putExtra("TRUYEN_ID", story.id)
                putExtra("USER_ID", userId)
                putExtra("TEN_TRUYEN", story.title)
                putExtra("TAC_GIA", story.author)
                putExtra("THE_LOAI", story.genre)
                putExtra("HINH_ANH", story.coverImage)
                putExtra("DESCRIPTION", story.description)
            }
            context.startActivity(intent)
        }
    }
}
