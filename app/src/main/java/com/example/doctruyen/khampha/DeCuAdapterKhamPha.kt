package com.example.doctruyen.khampha

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

class DeCuAdapterKhamPha(
    private val stories: List<Story>,
    private val userId: Int
) : RecyclerView.Adapter<DeCuAdapterKhamPha.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.storyImage)
        val title: TextView = view.findViewById(R.id.storyTitle)
        val genre: TextView = view.findViewById(R.id.storyGenre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_story_grid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = stories[position]

        holder.title.text = story.title
        holder.genre.text = story.genre

        holder.title.maxLines = if (position < 3) 1 else 2

        Glide.with(holder.image.context)
            .load(story.coverImage)
            .placeholder(R.drawable.kham_pha)
            .error(R.drawable.kham_pha)

            .into(holder.image)

        // Xử lý click để chuyển sang ChiTietTruyen
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

    override fun getItemCount() = stories.size
}
