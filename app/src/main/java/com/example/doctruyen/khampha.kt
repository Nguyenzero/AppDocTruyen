package com.example.doctruyen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.doctruyen.R
import com.example.doctruyen.taikhoan.taikhoan
import com.example.doctruyen.tutruyen.TuTruyen

class khampha : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.khampha)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tu_truyen -> {
                    val intent = Intent(this, TuTruyen::class.java)
                    startActivity(intent) // Thêm dòng này để chuyển trang
                    true
                }
                R.id.nav_kham_pha -> true
                R.id.nav_xep_hang -> {
                    // val intent = Intent(this, XepHang::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_tai_khoan -> {
                    val intent = Intent(this, taikhoan::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }

        }
    }
}