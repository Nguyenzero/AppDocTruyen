package com.example.doctruyen.tutruyen

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctruyen.R
import com.example.doctruyen.database.AppDatabase
import com.example.doctruyen.entity.Story
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryAdapter(
    private var storyList: MutableList<Story>,
    private val onItemClick: (Story) -> Unit,
    private val database: AppDatabase,
    private val userId: Int
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    fun updateData(newStories: List<Story>) {
        Log.d("StoryAdapter", "Cập nhật ${newStories.size} truyện vào adapter")
        storyList.clear()
        storyList.addAll(newStories)
        notifyDataSetChanged()
    }


    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgStory: ImageView = itemView.findViewById(R.id.imgStory)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvChapter: TextView = itemView.findViewById(R.id.tvChapter)
        val btnMoreOptions: ImageView = itemView.findViewById(R.id.btnMoreOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tutruyen, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = storyList[position]
        Log.d("StoryAdapter", "Bind truyện: ${story.title}")
        holder.tvTitle.text = story.title

        holder.btnMoreOptions.setOnClickListener { view ->
            val context = view.context

            // Hiển thị hộp thoại xác nhận xóa
            AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa truyện này khỏi Tủ truyện?")
                .setPositiveButton("Xóa") { dialog, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        database.bookmarkDao().deleteBookmark(userId, story.id)
                        withContext(Dispatchers.Main) {
                            onDeleteClick(story)
                        }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Hủy") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        CoroutineScope(Dispatchers.Main).launch {
            val lastReadChapter = withContext(Dispatchers.IO) {
                database.bookmarkDao().getLastReadChapterNumber(userId, story.id)
            }
            holder.tvChapter.text = if (lastReadChapter != null) "Đã đọc $lastReadChapter" else "Chưa đọc"
        }

        Glide.with(holder.itemView.context)
            .load(story.coverImage)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imgStory)

        holder.itemView.setOnClickListener { onItemClick(story) }
    }

    override fun getItemCount() = storyList.size

    fun onDeleteClick(story: Story) {
        val position = storyList.indexOf(story)
        if (position != -1) {
            storyList.removeAt(position) // Xóa truyện khỏi danh sách
            notifyItemRemoved(position) // Cập nhật RecyclerView
        }
    }
}