package com.example.doctruyen.taikhoan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.doctruyen.LoginActivity
import com.example.doctruyen.R
import com.example.doctruyen.Ranking.Ranking
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.khampha.Khampha
import com.example.doctruyen.tutruyen.TuTruyen
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class taikhoan : AppCompatActivity() {
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.taikhoan)

        val rootView = findViewById<View>(R.id.taikhoan)
        rootView.setOnApplyWindowInsetsListener { v, insets ->
            v.setPadding(0, insets.systemWindowInsetTop, 0, 0)
            insets
        }

        val txtUserName = findViewById<TextView>(R.id.txtUserName)
        val btnAccountInfo = findViewById<LinearLayout>(R.id.btnAccountInfo)
        val btnLogout = findViewById<LinearLayout>(R.id.btnLogout)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val btnDoiMatKhau = findViewById<LinearLayout>(R.id.btnDoiMatKhau)

        userId = intent.getIntExtra("USER_ID", -1)
        val db = AppDatabase.getDatabase(this)

        // Load tên người dùng
        lifecycleScope.launch {
            val user = db.userDao().getUserById(userId)
            user?.let {
                txtUserName.text = it.username
            }
        }

        // Bấm vào thông tin tài khoản
        btnAccountInfo.setOnClickListener {
            val intent = Intent(this, ThongTinTaiKhoan::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        // Logout
        btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnDoiMatKhau.setOnClickListener {
            val intent = Intent(this, DoiMatKhau::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }


        // Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_kham_pha -> {
                    startActivity(Intent(this, Khampha::class.java).putExtra("USER_ID", userId))
                    true
                }
                R.id.nav_xep_hang -> {
                    startActivity(Intent(this, Ranking::class.java).putExtra("USER_ID", userId))
                    true
                }
                R.id.nav_tu_truyen -> {
                    startActivity(Intent(this, TuTruyen::class.java).putExtra("USER_ID", userId))
                    true
                }
                else -> false
            }
        }
    }
}
