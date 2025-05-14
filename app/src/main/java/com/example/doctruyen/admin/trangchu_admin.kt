package com.example.doctruyen.admin

            import android.content.Intent
            import android.content.SharedPreferences
            import android.os.Bundle
            import android.view.Gravity
            import android.view.MenuItem
            import android.view.View
            import android.widget.ImageButton
            import android.widget.TextView
            import androidx.appcompat.app.AppCompatActivity
            import androidx.drawerlayout.widget.DrawerLayout
            import androidx.lifecycle.Observer
            import androidx.lifecycle.ViewModelProvider
            import com.example.doctruyen.LoginActivity
            import com.example.doctruyen.R
            import com.example.doctruyen.Ranking.BinhLuanFragment
            import com.example.doctruyen.Ranking.DeCuFragment
            import com.example.doctruyen.Ranking.LuotDocFragment
            import com.google.android.material.navigation.NavigationView

class trangchu_admin : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var adminViewModel: AdminViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trang_chu_admin)

        val rootView = findViewById<View>(R.id.adminLayout)
        rootView.setOnApplyWindowInsetsListener { v, insets ->
            v.setPadding(0, insets.systemWindowInsetTop, 0, 0) // Đẩy nội dung xuống dưới
            insets
        }

        // Khởi tạo ViewModel
        adminViewModel = ViewModelProvider(this).get(AdminViewModel::class.java)

        val tvTotalStories = findViewById<TextView>(R.id.tvTotalStories)
        val tvTotalUsers = findViewById<TextView>(R.id.tvTotalUsers)
        val tvTotalRatings = findViewById<TextView>(R.id.tvTotalRatings)
        val tvTotalChapters = findViewById<TextView>(R.id.tvTotalChapters)

        // Quan sát LiveData và cập nhật TextView khi dữ liệu thay đổi
        adminViewModel.totalStories.observe(this, Observer { total ->
            tvTotalStories.text = "📚 Tổng truyện: $total"
        })

        adminViewModel.totalUsers.observe(this, Observer { total ->
            tvTotalUsers.text = "👤 Người dùng: $total"
        })

        adminViewModel.totalRatings.observe(this, Observer { total ->
            tvTotalRatings.text = "⭐ Đánh giá: $total"
        })

        adminViewModel.totalChapters.observe(this, Observer { total ->
            tvTotalChapters.text = "📖 Tổng chương: $total"
        })

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val btnOpenMenu = findViewById<ImageButton>(R.id.btnOpenMenu)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        btnOpenMenu.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }

        navView.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction()
            .replace(R.id.deCuFragmentContainer, DeCuFragment())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.luotDocFragmentContainer, LuotDocFragment())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.danhgiaFragmentContainer, BinhLuanFragment())
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_manage_stories -> {
                val intent = Intent(this, quanlytruyen::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                logout()
            }
            R.id.nav_manage_users -> {
                val intent = Intent(this, QuanLyNguoiDung::class.java)
                startActivity(intent)
            }
            R.id.nav_danhgia -> {
                val intent = Intent(this, DanhSachTruyenDanhGia::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    private fun logout() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

