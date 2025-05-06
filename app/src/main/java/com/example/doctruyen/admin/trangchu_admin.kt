package com.example.doctruyen.admin

            import android.content.Intent
            import android.content.SharedPreferences
            import android.os.Bundle
            import android.view.Gravity
            import android.view.MenuItem
            import android.view.View
            import android.widget.ImageButton
            import androidx.appcompat.app.AppCompatActivity
            import androidx.drawerlayout.widget.DrawerLayout
            import com.example.doctruyen.LoginActivity
            import com.example.doctruyen.R
            import com.google.android.material.navigation.NavigationView

            class trangchu_admin : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    setContentView(R.layout.trang_chu_admin)


                    val rootView = findViewById<View>(R.id.adminLayout) // Thay rootView bằng ID của LinearLayout gốc
                    rootView.setOnApplyWindowInsetsListener { v, insets ->
                        v.setPadding(0, insets.systemWindowInsetTop, 0, 0) // Đẩy nội dung xuống dưới
                        insets
                    }

                    val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
                    val btnOpenMenu = findViewById<ImageButton>(R.id.btnOpenMenu)

                    val navView = findViewById<NavigationView>(R.id.nav_view)

                    btnOpenMenu.setOnClickListener {
                        drawerLayout.openDrawer(Gravity.LEFT)
                    }

                    navView.setNavigationItemSelectedListener(this)
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
                       R.id.nav_manage_users-> {
                           val  intent = Intent(this, QuanLyNguoiDung::class.java)
                            startActivity(intent)
                       }
                        R.id.nav_danhgia -> {
                            val intent = Intent(this, DanhSachTruyenDanhGia::class.java)
                            startActivity(intent)
                        }

//                        R.id.nav_my_stories -> {
//                            val intent = Intent(this, TuTruyen::class.java)
//                            startActivity(intent)
//                        }
//                        R.id.nav_ranking -> {
//                            val intent = Intent(this, Ranking::class.java)
//                            startActivity(intent)
//                        }
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