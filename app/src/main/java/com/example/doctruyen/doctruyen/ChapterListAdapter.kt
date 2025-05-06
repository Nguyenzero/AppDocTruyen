package com.example.doctruyen.doctruyen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R

import android.view.View


class ChapterListAdapter(
    private val chapters: List<String>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ChapterListAdapter.ChapterViewHolder>() {

    inner class ChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvChuong: TextView = itemView.findViewById(R.id.tvChuong)

        init {
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chuong, parent, false)
        return ChapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.tvChuong.text = chapters[position]
    }

    override fun getItemCount(): Int = chapters.size
}
