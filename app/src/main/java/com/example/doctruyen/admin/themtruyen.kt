package com.example.doctruyen.admin

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.Story
import kotlinx.coroutines.launch

class themtruyen : AppCompatActivity() {

    private lateinit var imgCover: ImageView
    private lateinit var edtImageUrl: EditText
    private lateinit var btnLoadImage: Button
    private lateinit var edtTenTruyen: EditText
    private lateinit var edtTacGia: EditText
    private lateinit var edtTheLoai: EditText
    private lateinit var spinnerTrangThai: Spinner
    private lateinit var edtMoTa: EditText
    private lateinit var btnThemTruyen: Button

    private var imageUrl: String? = null
    private var storyIdToEdit: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.them_truyen)

        val rootView = findViewById<View>(R.id.themtruyen)
        rootView.setOnApplyWindowInsetsListener { v, insets ->
            v.setPadding(0, insets.systemWindowInsetTop, 0, 0)
            insets
        }

        // Ánh xạ View
        imgCover = findViewById(R.id.imgCover)
        edtImageUrl = findViewById(R.id.edtImageUrl)
        btnLoadImage = findViewById(R.id.btnLoadImage)
        edtTenTruyen = findViewById(R.id.edtTenTruyen)
        edtTacGia = findViewById(R.id.edtTacGia)
        edtTheLoai = findViewById(R.id.edtTheLoai)
        spinnerTrangThai = findViewById(R.id.spinnerTrangThai)
        edtMoTa = findViewById(R.id.edtMoTa)
        btnThemTruyen = findViewById(R.id.btnThemTruyen)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // Kiểm tra Intent để chỉnh sửa
        storyIdToEdit = intent.getIntExtra("story_id", -1).takeIf { it != -1 }

        if (storyIdToEdit != null) {
            btnThemTruyen.text = "Lưu"
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                val story = db.storyDao().getStoryById(storyIdToEdit!!)
                if (story != null) {
                    edtTenTruyen.setText(story.title)
                    edtTacGia.setText(story.author)
                    edtTheLoai.setText(story.genre)
                    edtImageUrl.setText(story.coverImage)
                    edtMoTa.setText(story.description)
                    imageUrl = story.coverImage

                    Glide.with(this@themtruyen)
                        .load(imageUrl)
                        .into(imgCover)

                    val statusArray = resources.getStringArray(R.array.trang_thai_truyen)
                    val statusIndex = statusArray.indexOf(story.status)
                    if (statusIndex >= 0) {
                        spinnerTrangThai.setSelection(statusIndex)
                    }
                }
            }
        }

        // Xử lý tải ảnh từ URL
        btnLoadImage.setOnClickListener {
            val url = edtImageUrl.text.toString().trim()
            if (url.isNotEmpty()) {
                loadImageFromUrl(url)
            } else {
                showToast("Vui lòng nhập URL ảnh")
            }
        }

        btnThemTruyen.setOnClickListener {
            themTruyenVaoDatabase()
        }
    }

    private fun loadImageFromUrl(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.holo_red_light)
            .into(imgCover)

        imageUrl = url
    }

    private fun themTruyenVaoDatabase() {
        val tenTruyen = edtTenTruyen.text.toString().trim()
        val tacGia = edtTacGia.text.toString().trim()
        val theLoai = edtTheLoai.text.toString().trim()
        val trangThai = spinnerTrangThai.selectedItem.toString()
        val moTa = edtMoTa.text.toString().trim()

        if (tenTruyen.isEmpty() || tacGia.isEmpty() || theLoai.isEmpty() || moTa.isEmpty() || imageUrl.isNullOrEmpty()) {
            showToast("Vui lòng nhập đầy đủ thông tin")
            return
        }

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)

            if (storyIdToEdit == null) {
                // Thêm mới
                val story = Story(
                    title = tenTruyen,
                    author = tacGia,
                    genre = theLoai,
                    coverImage = imageUrl!!,
                    description = moTa,
                    status = trangThai
                )
                db.storyDao().insertStory(story)
                showToast("Thêm truyện thành công")
            } else {
                // Cập nhật
                val updatedStory = Story(
                    id = storyIdToEdit!!,
                    title = tenTruyen,
                    author = tacGia,
                    genre = theLoai,
                    coverImage = imageUrl!!,
                    description = moTa,
                    status = trangThai
                )
                db.storyDao().updateStory(updatedStory)
                showToast("Cập nhật truyện thành công")
            }
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

