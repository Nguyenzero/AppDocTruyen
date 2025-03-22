    package com.example.doctruyen.admin

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageButton
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import com.bumptech.glide.Glide
    import com.example.doctruyen.R
    import com.example.doctruyen.database.AppDatabase
    import com.example.doctruyen.entity.Story
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext


    class TruyenAdapter(
        private var listTruyen: List<Story>,
        private val onItemClick: (Story) -> Unit, // Khi nhấn vào truyện
        private val onDelete: (Story) -> Unit
    ) : RecyclerView.Adapter<TruyenAdapter.TruyenViewHolder>() {

        inner class TruyenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgTruyen: ImageView = itemView.findViewById(R.id.imgTruyen)
            val tvTenTruyen: TextView = itemView.findViewById(R.id.tvTenTruyen)
            val tvTheLoai: TextView = itemView.findViewById(R.id.tvTheLoai)
            val tvSoChap: TextView = itemView.findViewById(R.id.tvSoChap)
            val btnXoa: ImageButton = itemView.findViewById(R.id.btnXoa)

            fun bind(truyen: Story) {
                tvTenTruyen.text = truyen.title
                tvTheLoai.text = "Thể loại: ${truyen.genre}"
                Glide.with(itemView).load(truyen.coverImage).into(imgTruyen)

                // Khởi tạo database
                val db = AppDatabase.getDatabase(itemView.context)

                // Lấy số chương từ database và cập nhật UI
                CoroutineScope(Dispatchers.IO).launch {
                    val soChuong = db.chapterDao().getChapterCountByStoryId(truyen.id)
                    withContext(Dispatchers.Main) {
                        tvSoChap.text = "Số chương: $soChuong"
                    }
                }

                itemView.setOnClickListener { onItemClick(truyen) }
                btnXoa.setOnClickListener { onDelete(truyen) }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TruyenViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_truyen_admin, parent, false)
            return TruyenViewHolder(view)
        }

        override fun onBindViewHolder(holder: TruyenViewHolder, position: Int) {
            holder.bind(listTruyen[position])
        }

        override fun getItemCount(): Int = listTruyen.size

        fun updateData(newList: List<Story>) {
            listTruyen = newList
            notifyDataSetChanged()
        }
    }

