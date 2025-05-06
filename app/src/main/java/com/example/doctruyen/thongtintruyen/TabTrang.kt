package com.example.doctruyen.thongtintruyen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
class TabTrang(
    activity: ChiTietTruyen,
    private val description: String,
    private val truyenId: Int,
    private val userId: Int,
    private val storyTitle: String,
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GioiThieuFragment.newInstance(description, truyenId)
            1 -> DanhGiaFragment.newInstance(truyenId, userId) // <- Truyền userId
            2 -> DanhSachChuongFragment.newInstance(truyenId,userId,storyTitle)
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}



