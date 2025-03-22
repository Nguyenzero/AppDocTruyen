package com.example.doctruyen.thongtintruyen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R
import com.example.doctruyen.entity.Chapter

class ChuongAdapter(private var chuongList: List<Chapter>) :
    RecyclerView.Adapter<ChuongAdapter.ChuongViewHolder>() {

    class ChuongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvChuong: TextView = itemView.findViewById(R.id.tvChuong)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChuongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chuong, parent, false)
        return ChuongViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChuongViewHolder, position: Int) {
        val chapter = chuongList[position]
        holder.tvChuong.text = "Chương ${chapter.chapterNumber}: ${chapter.title}"
    }

    override fun getItemCount(): Int = chuongList.size

    fun updateData(newChuongList: List<Chapter>) {
        chuongList = newChuongList
        notifyDataSetChanged()
    }
}



