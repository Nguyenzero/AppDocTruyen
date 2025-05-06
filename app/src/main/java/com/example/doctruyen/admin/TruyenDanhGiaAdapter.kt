package com.example.doctruyen.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctruyen.R
import com.example.doctruyen.entity.Story

class TruyenDanhGiaAdapter(private var list: List<Pair<Story, Int>>) :
    RecyclerView.Adapter<TruyenDanhGiaAdapter.ViewHolder>() {

    private var onItemClickListener: ((Story) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTenTruyen: TextView = view.findViewById(R.id.tvTenTruyen)
        val tvSoDanhGia: TextView = view.findViewById(R.id.tvSoDanhGia)
        val imgAnhBia: ImageView = view.findViewById(R.id.imgAnhBiaTruyen)

        init {
            view.setOnClickListener {
                val story = list[adapterPosition].first
                onItemClickListener?.invoke(story)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_truyen_danh_gia_admin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (story, soDanhGia) = list[position]
        holder.tvTenTruyen.text = story.title
        holder.tvSoDanhGia.text = "$soDanhGia đánh giá"

        Glide.with(holder.itemView.context)
            .load(story.coverImage)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imgAnhBia)
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<Pair<Story, Int>>) {
        list = newList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Story) -> Unit) {
        onItemClickListener = listener
    }
}
