package com.example.doctruyen.TimKiem

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

class StoryAdapter(
    private val userId: Int,
    private var storiesWithStats: MutableList<Triple<Story, Int, Float>>
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    fun updateData(newList: List<Triple<Story, Int, Float>>) {
        storiesWithStats.clear()
        storiesWithStats.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        val tvViews: TextView = itemView.findViewById(R.id.tvViews)
        val imgCover: ImageView = itemView.findViewById(R.id.imgCover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (story, views, rating) = storiesWithStats[position]

        holder.tvTitle.text = story.title
        holder.tvAuthor.text = story.author
        holder.tvCategory.text = story.genre
        holder.tvRating.text = "⭐ %.1f".format(rating)
        holder.tvViews.text = "| views $views"

        Glide.with(holder.itemView.context)
            .load(story.coverImage)
            .into(holder.imgCover)



        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ChiTietTruyen::class.java).apply {
                putExtra("TRUYEN_ID", story.id)
                putExtra("USER_ID", userId)
                putExtra("TEN_TRUYEN", story.title ?: "Không có tên")
                putExtra("TAC_GIA", story.author ?: "Không rõ tác giả")
                putExtra("THE_LOAI", story.genre ?: "Chưa có thể loại")
                putExtra("HINH_ANH", story.coverImage ?: "")
                putExtra("DESCRIPTION", story.description)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = storiesWithStats.size
}