package com.example.doctruyen.TimKiem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.doctruyen.dao.StoryDao
import com.example.doctruyen.databinding.FragmentFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class FilterBottomSheetFragment(
    private val storyDao: StoryDao,
    private val onFilterApplied: (String?, String?, String?, String?) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FilterViewModel

    private val sortOptions = listOf( "Lượt đọc", "Số sao", "Đề cử")
    private val statusOptions = listOf("Đang ra", "Hoàn thành")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, FilterViewModelFactory(storyDao))[FilterViewModel::class.java]

        setupChips(binding.sortOptionsGroup, sortOptions)
        setupChips(binding.statusOptionsGroup, statusOptions)

        viewModel.allGenres.observe(viewLifecycleOwner) {
            setupChips(binding.categoryOptionsGroup, it)
        }
        viewModel.allAuthors.observe(viewLifecycleOwner) {
            setupChips(binding.authorOptionsGroup, it)
        }

        binding.btnApplyFilter.setOnClickListener {
            val sort = getSelectedChipText(binding.sortOptionsGroup)
            val status = getSelectedChipText(binding.statusOptionsGroup)
            val genre = getSelectedChipText(binding.categoryOptionsGroup)
            val author = getSelectedChipText(binding.authorOptionsGroup)

            Log.d("FilterSelection", "Sort: $sort, Status: $status, Genre: $genre, Author: $author")

            onFilterApplied(sort, status, genre, author)
            dismiss()
        }

        return binding.root
    }

    private fun setupChips(chipGroup: ChipGroup, options: List<String>) {
        chipGroup.isSingleSelection = true  // đảm bảo chỉ chọn một chip
        chipGroup.removeAllViews()
        options.forEach { option ->
            val chip = Chip(requireContext()).apply {
                text = option
                isCheckable = true
            }
            chipGroup.addView(chip)
        }
    }

    private fun getSelectedChipText(chipGroup: ChipGroup): String? {
        val chipId = chipGroup.checkedChipId
        return chipGroup.findViewById<Chip>(chipId)?.text?.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
