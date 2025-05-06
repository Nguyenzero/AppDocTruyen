    package com.example.doctruyen.admin

    import android.content.Intent
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
        private val onDelete: (Story) -> Unit,
        private val onEdit: (Story) -> Unit
    ) : RecyclerView.Adapter<TruyenAdapter.TruyenViewHolder>() {

        inner class TruyenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgTruyen: ImageView = itemView.findViewById(R.id.imgTruyen)
            val tvTenTruyen: TextView = itemView.findViewById(R.id.tvTenTruyen)
            val tvTheLoai: TextView = itemView.findViewById(R.id.tvTheLoai)
            val tvSoChap: TextView = itemView.findViewById(R.id.tvSoChap)
            val btnXoa: ImageButton = itemView.findViewById(R.id.btnXoa)
            val btnChinhSua: ImageButton = itemView.findViewById(R.id.btnChinhSua)

            fun bind(truyen: Story) {
                tvTenTruyen.text = truyen.title
                tvTheLoai.text = "Thể loại: ${truyen.genre}"
                Glide.with(itemView).load(truyen.coverImage).into(imgTruyen)

                val db = AppDatabase.getDatabase(itemView.context)
                CoroutineScope(Dispatchers.IO).launch {
                    val soChuong = db.chapterDao().getChapterCountByStoryId(truyen.id)
                    withContext(Dispatchers.Main) {
                        tvSoChap.text = "Số chương: $soChuong"
                    }
                }

                itemView.setOnClickListener { onItemClick(truyen) }
                btnXoa.setOnClickListener { onDelete(truyen) }
                btnChinhSua.setOnClickListener {
                    val intent = Intent(itemView.context, themtruyen::class.java)
                    intent.putExtra("story_id", truyen.id)  // Truyền ID truyện
                    itemView.context.startActivity(intent)
                }
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


