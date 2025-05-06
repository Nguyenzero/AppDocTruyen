package com.example.doctruyen.taikhoan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThongTinTaiKhoan : AppCompatActivity() {

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thong_tin_tai_khoan_users)

        userId = intent.getIntExtra("USER_ID", -1)

        val edtUsername = findViewById<EditText>(R.id.edtUsername)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val btnSave = findViewById<Button>(R.id.btnEditInfo)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        val userDao = AppDatabase.getDatabase(this).userDao()

        lifecycleScope.launch {
            val user = userDao.getUserById(userId)
            user?.let {
                edtUsername.setText(it.username)
                edtEmail.setText(it.email)
            }
        }

        btnSave.setOnClickListener {
            val newUsername = edtUsername.text.toString().trim()
            val newEmail = edtEmail.text.toString().trim()

            if (newUsername.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Vui lòng không để trống", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = userDao.getUserById(userId)
                if (user != null) {
                    val updatedUser = user.copy(username = newUsername, email = newEmail)
                    userDao.updateUser(updatedUser)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ThongTinTaiKhoan, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
