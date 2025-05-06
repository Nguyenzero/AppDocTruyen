package com.example.doctruyen.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.DanhGia
import com.example.doctruyen.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DanhGiaAdapter(
    private val context: Context,
    private var danhGiaList: List<DanhGia>,
    private val userMap: Map<Int, User>,
    private val onDelete: (DanhGia) -> Unit
) : RecyclerView.Adapter<DanhGiaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.tvUsername)
        val rating: TextView = view.findViewById(R.id.tvRating)
        val comment: TextView = view.findViewById(R.id.tvDanhGia)
        val time: TextView = view.findViewById(R.id.tvTime)
        val star: ImageView = view.findViewById(R.id.imgStar)
        val delete: Button = view.findViewById(R.id.btnDeleteDanhGia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_danh_gia_chi_tiet_admin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val danhGia = danhGiaList[position]
        val user = userMap[danhGia.user_id]

        holder.username.text = user?.username ?: "Ẩn danh"
        holder.rating.text = danhGia.rank.toString()
        holder.comment.text = danhGia.comment
        holder.time.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(danhGia.time))
        holder.star.setImageResource(R.drawable.star)

        holder.delete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa đánh giá này?")
                .setPositiveButton("Xóa") { _, _ ->
                    onDelete(danhGia)
                }
                .setNegativeButton("Hủy", null)
                .show()
        }
    }

    override fun getItemCount(): Int = danhGiaList.size

    fun updateDanhGiaList(newList: List<DanhGia>) {
        danhGiaList = newList
        notifyDataSetChanged()
    }
}
