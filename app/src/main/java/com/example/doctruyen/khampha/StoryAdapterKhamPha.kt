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
import com.example.doctruyen.dao.DanhGiaDao
import com.example.doctruyen.entity.Story
import com.example.doctruyen.thongtintruyen.ChiTietTruyen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryAdapterKhamPha(
    private var stories: List<Story>,
    private val userId: Int,
    private val danhGiaDao: DanhGiaDao
) : RecyclerView.Adapter<StoryAdapterKhamPha.StoryViewHolder>() {

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgTruyen: ImageView = itemView.findViewById(R.id.imgTruyen)
        val tvTenTruyen: TextView = itemView.findViewById(R.id.tvTenTruyen)
        val tvTheLoai: TextView = itemView.findViewById(R.id.tvTheLoai)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_khampha, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]

        // Hiển thị ảnh truyện
        Glide.with(holder.itemView.context)
            .load(story.coverImage)
            .placeholder(R.drawable.kham_pha)
            .error(R.drawable.kham_pha)
            .into(holder.imgTruyen)

        // Hiển thị thông tin tiêu đề và thể loại
        holder.tvTenTruyen.text = story.title
        holder.tvTheLoai.text = story.genre

        // Lấy số sao trung bình từ DanhGiaDao (nếu không có, hiển thị 0)


        // Xử lý click chuyển sang trang chi tiết truyện
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ChiTietTruyen::class.java).apply {
                putExtra("TRUYEN_ID", story.id)
                putExtra("USER_ID", userId)
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
