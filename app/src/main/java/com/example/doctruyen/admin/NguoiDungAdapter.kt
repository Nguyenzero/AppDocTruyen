package com.example.doctruyen.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R
import com.example.doctruyen.entity.User

class NguoiDungAdapter(
    private var users: List<User>,
    private val onDelete: (User) -> Unit,
    private val onEdit: (User) -> Unit
) : RecyclerView.Adapter<NguoiDungAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName = itemView.findViewById<TextView>(R.id.txtTenNguoiDung)
        private val txtEmail = itemView.findViewById<TextView>(R.id.txtEmail)
        private val txtRole = itemView.findViewById<TextView>(R.id.txtRole)
        private val btnDelete = itemView.findViewById<Button>(R.id.btnXoa)
        private val btnEdit = itemView.findViewById<Button>(R.id.btnSua)

        fun bind(user: User) {
            txtName.text = user.username
            txtEmail.text = user.email
            txtRole.text = if (user.role == "admin") "Admin" else "Người dùng"

            btnDelete.setOnClickListener { onDelete(user) }
            btnEdit.setOnClickListener { onEdit(user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nguoi_dung_admin, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size

    fun updateData(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }
}
