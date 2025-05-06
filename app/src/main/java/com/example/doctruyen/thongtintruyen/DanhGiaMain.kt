package com.example.doctruyen.thongtintruyen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.DanhGia

import kotlinx.coroutines.*


class DanhGiaMain : AppCompatActivity() {

    private lateinit var tvDiem: TextView
    private lateinit var seekBarDiem: SeekBar
    private lateinit var btnBack: ImageButton
    private lateinit var switchChiChamDiem: Switch
    private lateinit var edtNhanVat: EditText
    private lateinit var edtCotTruyen: EditText
    private lateinit var edtBoCuc: EditText
    private lateinit var edtNoiDungDanhGia: EditText
    private lateinit var btnDangDanhGia: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.danh_gia)

        // Ánh xạ view
        tvDiem = findViewById(R.id.tvDiem)
        seekBarDiem = findViewById(R.id.seekBarDiem)
        btnBack = findViewById(R.id.btnBack)
        switchChiChamDiem = findViewById(R.id.switchChiChamDiem)
        edtNhanVat = findViewById(R.id.edtNhanVat)
        edtCotTruyen = findViewById(R.id.edtCotTruyen)
        edtBoCuc = findViewById(R.id.edtBoCuc)
        edtNoiDungDanhGia = findViewById(R.id.edtNoiDungDanhGia)
        btnDangDanhGia = findViewById(R.id.btnDangDanhGia)

        // SeekBar điểm
        seekBarDiem.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val diem = progress / 10.0
                tvDiem.text = "Chấm điểm nội dung truyện: %.1f điểm".format(diem)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Nút back
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Mặc định: bật nút "Đăng"
        btnDangDanhGia.isEnabled = true
        btnDangDanhGia.setBackgroundColor(resources.getColor(R.color.black))

        // Switch "Chỉ chấm điểm"
        switchChiChamDiem.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                edtNhanVat.visibility = EditText.GONE
                edtCotTruyen.visibility = EditText.GONE
                edtBoCuc.visibility = EditText.GONE
                edtNoiDungDanhGia.visibility = EditText.GONE
            } else {
                edtNhanVat.visibility = EditText.VISIBLE
                edtCotTruyen.visibility = EditText.VISIBLE
                edtBoCuc.visibility = EditText.VISIBLE
                edtNoiDungDanhGia.visibility = EditText.VISIBLE
            }
        }

        // KHÔNG cần kiểm tra độ dài nữa => chỉ kiểm tra có chữ hay không
        edtNoiDungDanhGia.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val coNoiDung = !s.isNullOrBlank() || switchChiChamDiem.isChecked
                btnDangDanhGia.isEnabled = coNoiDung
                btnDangDanhGia.setBackgroundColor(resources.getColor(R.color.black))
            }
        })

        // Nút "Đăng"
        btnDangDanhGia.setOnClickListener {
            val rank = seekBarDiem.progress / 10f

            val isOnlyRank = switchChiChamDiem.isChecked

            val comment = if (isOnlyRank) {
                ""
            } else {
                val danhGiaList = mutableListOf<String>()
                if (!edtNhanVat.text.isNullOrBlank()) {
                    danhGiaList.add(" ${edtNhanVat.text}")
                }
                if (!edtCotTruyen.text.isNullOrBlank()) {
                    danhGiaList.add(" ${edtCotTruyen.text}")
                }
                if (!edtBoCuc.text.isNullOrBlank()) {
                    danhGiaList.add(" ${edtBoCuc.text}")
                }
                if (!edtNoiDungDanhGia.text.isNullOrBlank()) {
                    danhGiaList.add(" ${edtNoiDungDanhGia.text}")
                }
                danhGiaList.joinToString("\n")
            }

            val userId = intent.getIntExtra("userId", -1)
            val storyId = intent.getIntExtra("storyId", -1)

            Log.d("DanhGia", "userId: $userId, storyId: $storyId")

            if (userId == -1 || storyId == -1) {
                Toast.makeText(this, "Thiếu thông tin người dùng hoặc truyện!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val danhGia = DanhGia(
                user_id = userId,
                story_id = storyId,
                rank = rank,
                comment = comment,
                time = System.currentTimeMillis(),
                likes = 0
            )

            GlobalScope.launch(Dispatchers.IO) {
                val db = AppDatabase.getDatabase(applicationContext)
                db.danhGiaDao().insertDanhGia(danhGia)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DanhGiaMain, "Gửi đánh giá thành công!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

    }

}

