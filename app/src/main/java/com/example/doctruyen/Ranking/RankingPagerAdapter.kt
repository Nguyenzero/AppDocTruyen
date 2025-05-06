package com.example.doctruyen.Ranking

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class RankingPagerAdapter(fragment: Fragment, private val itemCount: Int) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = itemCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LuotDocFragment()   // Tab Lượt đọc
            2 -> DeCuFragment()      // Tab Đề cử
            1 -> BinhLuanFragment()   // Tab Đánh giá
             // Tab Yêu thích
            else -> throw IllegalStateException("Invalid position")
        }
    }
}
