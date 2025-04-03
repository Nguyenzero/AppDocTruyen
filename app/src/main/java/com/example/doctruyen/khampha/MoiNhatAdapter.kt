package com.example.doctruyen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctruyen.R
import com.example.doctruyen.entity.Story


interface OnTruyenMoiClickListener {
    fun onTruyenMoiClick(truyen: Story)
}
class MoiNhatAdapter(
    private var stories: List<Story>,
    private val listener: OnTruyenMoiClickListener
) : RecyclerView.Adapter<MoiNhatAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageMoiNhat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_moi_nhat, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val story = stories[position]
        Glide.with(holder.itemView.context)
            .load(story.coverImage)
            .placeholder(R.drawable.kham_pha)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            listener.onTruyenMoiClick(story) // Gọi callback
        }
    }

    override fun getItemCount(): Int = stories.size

    fun updateData(newStories: List<Story>) {
        stories = newStories
        notifyDataSetChanged()
    }
}

