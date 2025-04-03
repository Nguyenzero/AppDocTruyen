package com.example.doctruyen.taikhoan

                            import android.content.Intent
                            import android.content.SharedPreferences
                            import android.os.Bundle
                            import android.view.View
                            import android.widget.LinearLayout
                            import android.widget.TextView
                            import androidx.appcompat.app.AppCompatActivity
                            import com.example.doctruyen.LoginActivity
                            import com.example.doctruyen.R
                            import com.example.doctruyen.khampha.Khampha
                            import com.example.doctruyen.tutruyen.TuTruyen


                            import com.google.android.material.bottomnavigation.BottomNavigationView

                            class taikhoan : AppCompatActivity() {
                                override fun onCreate(savedInstanceState: Bundle?) {
                                    super.onCreate(savedInstanceState)
                                    setContentView(R.layout.taikhoan)

                                    val rootView = findViewById<View>(R.id.taikhoan) // Thay rootView bằng ID của LinearLayout gốc
                                    rootView.setOnApplyWindowInsetsListener { v, insets ->
                                        v.setPadding(0, insets.systemWindowInsetTop, 0, 0) // Đẩy nội dung xuống dưới
                                        insets
                                    }

                                    val txtUserName = findViewById<TextView>(R.id.txtUserName)
                                    val txtNotificationBadge = findViewById<TextView>(R.id.txtNotificationBadge)
                                    val btnLogout = findViewById<LinearLayout>(R.id.btnLogout)
                                    // Lấy thông tin người dùng từ SharedPreferences
                                    val sharedPreferences: SharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
                                    val userEmail = sharedPreferences.getString("email", "Người dùng")
                                    val userRole = sharedPreferences.getString("role", "user") // "user" hoặc "admin"

                                    // Cập nhật UI
                                    txtUserName.text = userEmail
                                    if (userRole == "admin") {
                                        txtNotificationBadge.text = "admin"
                                    }

                                    val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

                                    btnLogout.setOnClickListener {
                                        // Xóa thông tin người dùng khỏi SharedPreferences
                                        val editor = sharedPreferences.edit()
                                        editor.clear()
                                        editor.apply()

                                        // Chuyển về trang đăng nhập
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }

                                    bottomNavigationView.setOnItemSelectedListener { item ->
                                        when (item.itemId) {
                                            R.id.nav_kham_pha -> {
                                                val intent = Intent(this, Khampha::class.java)
                                                startActivity(intent)
                                                true
                                            }
                                            R.id.nav_xep_hang -> {
                                                // val intent = Intent(this, XepHang::class.java)
                                                startActivity(intent)
                                                true
                                            }
                                            R.id.nav_tu_truyen -> {
                                                val intent = Intent(this, TuTruyen::class.java)
                                                startActivity(intent)
                                                true
                                            }
                                            else -> false
                                        }
                                    }
                                }
                            }