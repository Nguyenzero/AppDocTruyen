package com.example.doctruyen.Ranking

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.doctruyen.R
import com.example.doctruyen.TimKiem.TimKiemActivity
import com.example.doctruyen.khampha.Khampha
import com.example.doctruyen.taikhoan.taikhoan
import com.example.doctruyen.tutruyen.TuTruyen
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import kotlin.jvm.java

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


        val etSearch = findViewById<EditText>(R.id.etSearch)
        etSearch.isFocusable = false
        etSearch.isClickable = true
        etSearch.setOnClickListener {
            val intent = Intent(this, TimKiemActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
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
    private fun showBottomSheet(title: String, items: List<String>) {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_filter, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)

        val titleText = view.findViewById<TextView>(R.id.titleFilter)
        val listView = view.findViewById<ListView>(R.id.listFilterOptions)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        titleText.text = title

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selected = items[position]
            // TODO: Xử lý khi chọn mục
            dialog.dismiss()
        }

        dialog.show()
    }

}
