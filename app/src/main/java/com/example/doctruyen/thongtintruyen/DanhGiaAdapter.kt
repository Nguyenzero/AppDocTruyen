package com.example.doctruyen.thongtintruyen

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.DanhGia
import com.example.doctruyen.setExpandableText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DanhGiaAdapter(
    private val danhGiaList: List<DanhGia>,
    private val context: Context
) : RecyclerView.Adapter<DanhGiaAdapter.DanhGiaViewHolder>() {

    inner class DanhGiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTenNguoiDung: TextView = itemView.findViewById(R.id.tvTenNguoiDung)
        val tvSoSao: TextView = itemView.findViewById(R.id.tvSoSao)
        val tvSoChuong: TextView = itemView.findViewById(R.id.tvSoChuong)
        val tvNoiDungDanhGia: TextView = itemView.findViewById(R.id.tvNoiDungDanhGia)
        val tvThoiGian: TextView = itemView.findViewById(R.id.tvThoiGian)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DanhGiaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_danh_gia, parent, false)
        return DanhGiaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DanhGiaViewHolder, position: Int) {
        val danhGia = danhGiaList[position]

        // Hiển thị thông tin đánh giá
        holder.tvSoSao.text = danhGia.rank.toString()
        holder.tvNoiDungDanhGia.setExpandableText(danhGia.comment)
        holder.tvThoiGian.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(danhGia.time)

        // Lấy thông tin người dùng và số chương đã đọc
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(context)

            val user = db.userDao().getUserById(danhGia.user_id)
            val lastChapter = db.bookmarkDao().getLastReadChapterNumber(danhGia.user_id, danhGia.story_id)

            Log.d("BookmarkDebug", "UserID: ${danhGia.user_id}, StoryID: ${danhGia.story_id}, Chapter: $lastChapter")

            withContext(Dispatchers.Main) {
                holder.tvTenNguoiDung.text = user?.username ?: "Người dùng"
                holder.tvSoChuong.text = if (lastChapter != null) "• Đã đọc: ${lastChapter}c" else "• Đã đọc: 0c"
            }
        }
    }

    override fun getItemCount(): Int = danhGiaList.size
}
