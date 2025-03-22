package com.example.doctruyen.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R
import com.example.doctruyen.entity.Chapter

class ChuongAdapter(
    private val listChuong: MutableList<Chapter>,
    private val onEditClick: (Chapter) -> Unit,
    private val onDeleteClick: (Chapter) -> Unit
) : RecyclerView.Adapter<ChuongAdapter.ChuongViewHolder>() {

    inner class ChuongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSoChuong: TextView = itemView.findViewById(R.id.tvSoChuong)
        val tvTenChuong: TextView = itemView.findViewById(R.id.tvTenChuong)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChuongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_them_chuong, parent, false)
        return ChuongViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChuongViewHolder, position: Int) {
        val chapter = listChuong[position]
        holder.tvSoChuong.text = "Chương ${chapter.chapterNumber}"
        holder.tvTenChuong.text = chapter.title
        holder.btnEdit.setOnClickListener { onEditClick(chapter) }
        holder.btnDelete.setOnClickListener { onDeleteClick(chapter) }
    }

    override fun getItemCount(): Int = listChuong.size
}
