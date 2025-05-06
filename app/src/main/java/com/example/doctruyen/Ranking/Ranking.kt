package com.example.doctruyen.Ranking

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.doctruyen.R
import com.example.doctruyen.khampha.Khampha
import com.example.doctruyen.taikhoan.taikhoan
import com.example.doctruyen.tutruyen.TuTruyen
import com.google.android.material.bottomnavigation.BottomNavigationView

class Ranking : AppCompatActivity() {
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ranking)


        userId = intent.getIntExtra("USER_ID", -1)

        // Load RankingFragment vào container
        supportFragmentManager.beginTransaction()
            .replace(R.id.ranking, RankingFragment())
            .commit()


           val rootView = findViewById<View>(R.id.ranking)


            rootView.setOnApplyWindowInsetsListener { v, insets ->
            v.setPadding(0, insets.systemWindowInsetTop, 0, 0) // Đẩy nội dung xuống dưới
            insets
        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tu_truyen -> {
                    val intent = Intent(this, TuTruyen::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    true
                }
                R.id.nav_kham_pha -> {
                    val intent = Intent(this, Khampha::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    true
                }
                R.id.nav_tai_khoan -> {
                    val intent = Intent(this, taikhoan::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


    }
}
