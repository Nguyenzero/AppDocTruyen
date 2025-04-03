package com.example.doctruyen.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctruyen.R
import com.example.doctruyen.entity.Story
import com.example.doctruyen.thongtintruyen.ChiTietTruyen



class StoryAdapterKhamPha(private var stories: List<Story>) :
    RecyclerView.Adapter<StoryAdapterKhamPha.StoryViewHolder>() {

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgTruyen: ImageView = itemView.findViewById(R.id.imgTruyen)
        val tvTenTruyen: TextView = itemView.findViewById(R.id.tvTenTruyen)
        val tvTheLoai: TextView = itemView.findViewById(R.id.tvTheLoai)

//        val btnDoc: Button = itemView.findViewById(R.id.btnDoc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_khampha, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]

        // Hiển thị ảnh, tên truyện, thể loại
        Glide.with(holder.itemView.context)
            .load(story.coverImage)
            .placeholder(R.drawable.kham_pha)
            .error(R.drawable.kham_pha)
            .into(holder.imgTruyen)

        holder.tvTenTruyen.text = story.title
        holder.tvTheLoai.text = story.genre

        // Sự kiện click vào item
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ChiTietTruyen::class.java).apply {
                putExtra("TRUYEN_ID", story.id)
                putExtra("TEN_TRUYEN", story.title ?: "Không có tên")
                putExtra("TAC_GIA", story.author ?: "Không rõ tác giả")
                putExtra("THE_LOAI", story.genre ?: "Chưa có thể loại")
                putExtra("HINH_ANH", story.coverImage ?: "")
                putExtra("DESCRIPTION", story.description)
            }
            context.startActivity(intent)
        }



    }
    override fun getItemCount(): Int = stories.size

    fun updateData(newStories: List<Story>) {
        stories = newStories
        notifyDataSetChanged()
    }
}

