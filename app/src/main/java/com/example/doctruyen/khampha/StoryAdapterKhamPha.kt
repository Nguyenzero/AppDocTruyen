package com.example.doctruyen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctruyen.R
import com.example.doctruyen.entity.Story

class StoryAdapterKhamPha(private var stories: List<Story>) :
    RecyclerView.Adapter<StoryAdapterKhamPha.StoryViewHolder>() {

    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgTruyen: ImageView = itemView.findViewById(R.id.imgTruyen)
        val tvTenTruyen: TextView = itemView.findViewById(R.id.tvTenTruyen)
        val tvTheLoai: TextView = itemView.findViewById(R.id.tvTheLoai)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_khampha, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        Glide.with(holder.itemView.context)
            .load(story.coverImage) // Ảnh lấy từ Room Database
            .placeholder(R.drawable.kham_pha)
            .error(R.drawable.kham_pha)
            .into(holder.imgTruyen)
        holder.tvTenTruyen.text = story.title
        holder.tvTheLoai.text = story.genre
    }

    override fun getItemCount(): Int = stories.size

    fun updateData(newStories: List<Story>) {
        stories = newStories
        notifyDataSetChanged()
    }
}
