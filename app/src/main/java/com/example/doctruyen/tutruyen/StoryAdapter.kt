package com.example.doctruyen.tutruyen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R

class StoryAdapter(private val storyList: List<Story>) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    // nhớ xóa cái field này đi
    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgStory: ImageView = itemView.findViewById(R.id.imgStory)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvGenre: TextView = itemView.findViewById(R.id.tvGenre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tutruyen, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = storyList[position]
        holder.imgStory.setImageResource(story.imageResId)
        holder.tvTitle.text = story.title
        holder.tvGenre.text = story.genre
    }

    override fun getItemCount() = storyList.size
}