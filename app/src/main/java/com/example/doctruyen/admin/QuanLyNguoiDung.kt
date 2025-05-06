package com.example.doctruyen.admin

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.User
import kotlinx.coroutines.launch

class QuanLyNguoiDung : AppCompatActivity() {

    private lateinit var recyclerNguoiDung: RecyclerView
    private lateinit var adapter: NguoiDungAdapter
    private lateinit var db: AppDatabase
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quan_ly_nguoi_dung_admin)

        recyclerNguoiDung = findViewById(R.id.recyclerNguoiDung)
        btnBack = findViewById(R.id.btnBack)
        db = AppDatabase.getDatabase(this)

        adapter = NguoiDungAdapter(
            users = emptyList(),
            onDelete = { user -> deleteUser(user) },
            onEdit = { user -> toggleUserRole(user) }
        )

        recyclerNguoiDung.layoutManager = LinearLayoutManager(this)
        recyclerNguoiDung.adapter = adapter

        btnBack.setOnClickListener { finish() }

        loadUsers()
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            val users = db.userDao().getAllUsers()
            adapter.updateData(users)
        }
    }

    private fun deleteUser(user: User) {
        lifecycleScope.launch {
            db.userDao().deleteUser(user)
            loadUsers()
            Toast.makeText(this@QuanLyNguoiDung, "Đã xóa người dùng", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleUserRole(user: User) {
        lifecycleScope.launch {
            val newRole = if (user.role == "admin") "user" else "admin"
            val updatedUser = user.copy(role = newRole)
            db.userDao().updateUser(updatedUser)
            loadUsers()
            Toast.makeText(this@QuanLyNguoiDung, "Chuyển vai trò thành ${newRole}", Toast.LENGTH_SHORT).show()
        }
    }
}
