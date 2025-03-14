package com.example.doctruyen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.doctruyen.admin.trangchu_admin
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.khampha.Khampha




import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        val database = AppDatabase.getDatabase(this)

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = database.userDao().getUser(email, password)
                runOnUiThread {
                    if (user != null) {
                        // Save user email to SharedPreferences
                        val sharedPreferences: SharedPreferences =
                            getSharedPreferences("UserData", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("email", user.email)
                        editor.putString("role", user.role)
                        editor.apply()

                        Toast.makeText(
                            this@LoginActivity,
                            "Đăng nhập thành công!",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = if (user.role == "admin") {
                            Intent(this@LoginActivity, trangchu_admin::class.java)
                        } else {
                            Intent(this@LoginActivity, Khampha::class.java)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Sai tài khoản hoặc mật khẩu!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}