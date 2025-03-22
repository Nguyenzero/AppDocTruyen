package com.example.doctruyen.thongtintruyen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.doctruyen.R

class GioiThieuFragment : Fragment() {

    private var description: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            description = it.getString("DESCRIPTION")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gioi_thieu, container, false)
        val tvGioiThieu = view.findViewById<TextView>(R.id.tvGioiThieu)
        tvGioiThieu.text = description ?: "Chưa có mô tả"
        return view
    }

    companion object {
        fun newInstance(description: String): GioiThieuFragment {
            val fragment = GioiThieuFragment()
            val args = Bundle()
            args.putString("DESCRIPTION", description)
            fragment.arguments = args
            return fragment
        }
    }
}


