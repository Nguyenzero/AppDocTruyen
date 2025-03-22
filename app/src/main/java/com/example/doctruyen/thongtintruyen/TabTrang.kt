package com.example.doctruyen.thongtintruyen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
class TabTrang(activity: ChiTietTruyen, private val description: String, private val truyenId: Int)
    : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GioiThieuFragment.newInstance(description)
            1 -> DanhGiaFragment()
            2 -> DanhSachChuongFragment.newInstance(truyenId)
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}


