package com.example.doctruyen.taikhoan

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class DoiMatKhau : AppCompatActivity() {

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.doi_mat_khau)

        userId = intent.getIntExtra("USER_ID", -1)

        val edtCurrentPassword = findViewById<TextInputEditText>(R.id.edtCurrentPassword)
        val edtNewPassword = findViewById<TextInputEditText>(R.id.edtNewPassword)
        val edtConfirmPassword = findViewById<TextInputEditText>(R.id.edtConfirmPassword)
        val btnSave = findViewById<Button>(R.id.btnSavePassword)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            val currentPassword = edtCurrentPassword.text.toString().trim()
            val newPassword = edtNewPassword.text.toString().trim()
            val confirmPassword = edtConfirmPassword.text.toString().trim()

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Xác nhận mật khẩu không khớp!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = userDao.getUserById(userId)

                if (user == null) {
                    Toast.makeText(this@DoiMatKhau, "Người dùng không tồn tại!", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if (user.password != currentPassword) {
                    Toast.makeText(this@DoiMatKhau, "Mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if (newPassword == currentPassword) {
                    Toast.makeText(this@DoiMatKhau, "Mật khẩu mới không được trùng mật khẩu hiện tại!", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Cập nhật mật khẩu
                val updatedUser = user.copy(password = newPassword)
                userDao.updateUser(updatedUser)

                Toast.makeText(this@DoiMatKhau, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
