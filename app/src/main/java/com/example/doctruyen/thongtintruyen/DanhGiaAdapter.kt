package com.example.doctruyen.thongtintruyen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R

class DanhGiaAdapter(private val danhGiaList: List<String>) :
    RecyclerView.Adapter<DanhGiaAdapter.DanhGiaViewHolder>() {

    class DanhGiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDanhGia: TextView = itemView.findViewById(R.id.tvDanhGia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DanhGiaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_danh_gia, parent, false)
        return DanhGiaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DanhGiaViewHolder, position: Int) {
        holder.tvDanhGia.text = danhGiaList[position]
    }

    override fun getItemCount(): Int = danhGiaList.size
}
