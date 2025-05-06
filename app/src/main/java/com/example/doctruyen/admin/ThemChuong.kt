package com.example.doctruyen.admin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.Chapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThemChuong : AppCompatActivity() {

    private lateinit var tvTenTruyen: TextView
    private lateinit var tvTongSoChuong: TextView
    private lateinit var imgCover: ImageView
    private lateinit var rvDanhSachChuong: RecyclerView
    private lateinit var btnThemChuong: FloatingActionButton // SỬA LỖI Ở ĐÂY
    private lateinit var adapter: ChuongAdapter

    private var listChuong = mutableListOf<Chapter>()
    private var storyId: Int = 0
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.them_chuong)

        val rootView = findViewById<View>(R.id.themchuong) // Thay rootView bằng ID của LinearLayout gốc
        rootView.setOnApplyWindowInsetsListener { v, insets ->
            v.setPadding(0, insets.systemWindowInsetTop, 0, 0) // Đẩy nội dung xuống dưới
            insets
        }
        db = AppDatabase.getDatabase(this)

        tvTenTruyen = findViewById(R.id.tvTenTruyen)
        tvTongSoChuong = findViewById(R.id.tvTongSoChuong)
        imgCover = findViewById(R.id.imgCover)
        rvDanhSachChuong = findViewById(R.id.rvDanhSachChuong)
        btnThemChuong = findViewById(R.id.btnThemChuong)


        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }


        // Nhận dữ liệu từ QuanLyTruyen
        storyId = intent.getIntExtra("storyId", 0)
        val storyTitle = intent.getStringExtra("storyTitle")
        val storyCover = intent.getStringExtra("storyCover")


        tvTenTruyen.text = storyTitle
        Glide.with(this).load(storyCover).into(imgCover)

        adapter = ChuongAdapter(listChuong, onEditClick = { editChuong(it) }, onDeleteClick = { deleteChuong(it) })
        rvDanhSachChuong.layoutManager = LinearLayoutManager(this)
        rvDanhSachChuong.adapter = adapter

        btnThemChuong.setOnClickListener { showBottomSheetThemChuong() }

        loadDanhSachChuong()
    }

    private fun loadDanhSachChuong() {
        lifecycleScope.launch(Dispatchers.IO) { // Chạy trên luồng IO
            try {
                val danhSachChuong = db.chapterDao().getChaptersByStoryId(storyId)

                withContext(Dispatchers.Main) { // Cập nhật UI trên Main Thread
                    listChuong.clear()
                    listChuong.addAll(danhSachChuong)
                    adapter.notifyDataSetChanged()
                    tvTongSoChuong.text = "Tổng số chương: ${listChuong.size}"
                }
            } catch (e: Exception) {
                Log.e("ThemChuong", "Lỗi khi tải danh sách chương", e)
            }
        }
    }



    private fun showBottomSheetThemChuong() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_them_chuong, null)

        val edtTitle = view.findViewById<EditText>(R.id.edtTitle)
        val edtContent = view.findViewById<EditText>(R.id.edtContent)
        val btnLuuChuong = view.findViewById<Button>(R.id.btnLuuChuong)

        btnLuuChuong.setOnClickListener {
            val title = edtTitle.text.toString().trim()
            val content = edtContent.text.toString().trim()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val newChapter = Chapter(
                    storyId = storyId,
                    chapterNumber = listChuong.size + 1,
                    title = title,
                    content = content
                )

                lifecycleScope.launch {
                    db.chapterDao().insertChapter(newChapter) // Lưu chương vào database
                    loadDanhSachChuong() // Cập nhật danh sách chương
                    showToast("Đã thêm chương thành công!")

                    runOnUiThread {
                        adapter.notifyDataSetChanged() // Đảm bảo RecyclerView cập nhật
                    }

                    bottomSheetDialog.dismiss() // Đóng BottomSheetDialog sau khi lưu
                }
            } else {
                showToast("Vui lòng nhập đầy đủ thông tin!")
            }
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun editChuong(chapter: Chapter) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_them_chuong, null)

        val edtTitle = view.findViewById<EditText>(R.id.edtTitle)
        val edtContent = view.findViewById<EditText>(R.id.edtContent)
        val btnLuuChuong = view.findViewById<Button>(R.id.btnLuuChuong)

        // Gán dữ liệu chương vào EditText
        edtTitle.setText(chapter.title)
        edtContent.setText(chapter.content)

        btnLuuChuong.text = "Cập nhật chương" // Đổi text cho rõ ràng

        btnLuuChuong.setOnClickListener {
            val newTitle = edtTitle.text.toString().trim()
            val newContent = edtContent.text.toString().trim()

            if (newTitle.isNotEmpty() && newContent.isNotEmpty()) {
                val updatedChapter = chapter.copy(
                    title = newTitle,
                    content = newContent
                )

                lifecycleScope.launch {
                    db.chapterDao().insertChapter(updatedChapter) // Vì dùng OnConflictStrategy.REPLACE nên sẽ update
                    loadDanhSachChuong()
                    showToast("Cập nhật chương thành công!")
                    bottomSheetDialog.dismiss()
                }
            } else {
                showToast("Vui lòng nhập đầy đủ thông tin!")
            }
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun deleteChuong(chapter: Chapter) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Xác nhận xóa chương")
        builder.setMessage("Bạn có chắc muốn xóa chương '${chapter.title}' không?")

        builder.setPositiveButton("Xóa") { dialog, _ ->
            lifecycleScope.launch(Dispatchers.IO) {
                db.chapterDao().deleteChapter(chapter)

                withContext(Dispatchers.Main) {
                    showToast("Đã xóa chương thành công!")
                    loadDanhSachChuong()
                }
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

}
