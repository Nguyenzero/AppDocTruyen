package com.example.doctruyen.amdin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctruyen.R
import com.example.doctruyen.entity.Story

class TruyenAdapter(
    private var listTruyen: List<Story>,
    private val onDelete: (Story) -> Unit
) : RecyclerView.Adapter<TruyenAdapter.TruyenViewHolder>() {

    class TruyenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgTruyen: ImageView = itemView.findViewById(R.id.imgTruyen)
        val tvTenTruyen: TextView = itemView.findViewById(R.id.tvTenTruyen)
        val tvTheLoai: TextView = itemView.findViewById(R.id.tvTheLoai)
        val tvSoChap: TextView = itemView.findViewById(R.id.tvSoChap)
        val btnXoa: ImageButton = itemView.findViewById(R.id.btnXoa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TruyenViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_truyen_admin, parent, false)
        return TruyenViewHolder(view)
    }

    override fun onBindViewHolder(holder: TruyenViewHolder, position: Int) {
        val truyen = listTruyen[position]

        Glide.with(holder.itemView.context)
            .load(truyen.coverImage)
            .placeholder(R.drawable.kham_pha)
            .error(R.drawable.khampha)
            .into(holder.imgTruyen)

        holder.tvTenTruyen.text = truyen.title
        holder.tvTheLoai.text = "Thể loại: ${truyen.genre}"
        holder.tvSoChap.text = "Số chương: 100"

        holder.btnXoa.setOnClickListener { onDelete(truyen) }
    }

    override fun getItemCount() = listTruyen.size

    fun updateData(newList: List<Story>) {
        listTruyen = newList
        notifyDataSetChanged()
    }
}
