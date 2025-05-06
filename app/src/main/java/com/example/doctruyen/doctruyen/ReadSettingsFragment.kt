    package com.example.doctruyen.doctruyen

    import android.content.Intent
    import android.graphics.Color
    import android.graphics.drawable.GradientDrawable
    import android.os.Bundle
    import android.util.Log
    import android.view.*
    import android.widget.*
    import com.example.doctruyen.R
    import com.google.android.material.bottomsheet.BottomSheetDialogFragment

    class ReadSettingsFragment : BottomSheetDialogFragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = inflater.inflate(R.layout.fragment_read_settings, container, false)

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val activity = activity as? ReadStory ?: return

            val seekBarFontSize = view.findViewById<SeekBar>(R.id.seekBarFontSize)
            val spinnerFont = view.findViewById<Spinner>(R.id.spinnerFont)
            val colorPickerLayout = view.findViewById<LinearLayout>(R.id.colorPickerLayout)
            val btnListenStory = view.findViewById<Button>(R.id.btnListenStory)



            val storyId = arguments?.getInt("storyId") ?: -1 // Nhận id truyện
            val chapterId = arguments?.getInt("chapterId") ?: -1
            Log.d("ReadSettingsFragment", "chapterId sau khi nhận: $chapterId")  // Thêm log để kiểm tra giá trị


            btnListenStory.setOnClickListener {
                val intent = Intent(requireContext(), ListenStoryActivity::class.java).apply {

                    putExtra("autoPlay", false)
                    putExtra("storyId", storyId) // Truyền id truyện
                    putExtra("currentChapterIndex", chapterId) // Truyền số chương hiện tại
                }
                startActivity(intent)
                dismiss()
            }



            // Màu nền
            val colors = listOf("#FFFFFF", "#000000", "#F5F5DC", "#FFEB3B", "#E0F7FA")
            colors.forEach { hex ->
                val colorView = View(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(100, 100).apply {
                        setMargins(16, 0, 16, 0)
                    }
                    background = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = 20f
                        setColor(Color.parseColor(hex))
                    }
                    setOnClickListener {
                        activity.thayDoiMauNen(hex)
                        dismiss()
                    }
                }
                colorPickerLayout.addView(colorView)
            }

            // Cỡ chữ
            seekBarFontSize.max = 40
            seekBarFontSize.progress = 16
            seekBarFontSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (progress >= 10) {
                        activity.thayDoiCoChu(progress.toFloat())
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            // Font chữ
            val fonts = arrayOf("Mặc định", "Sans Serif", "Serif", "Monospace")
            spinnerFont.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fonts).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinnerFont.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    activity.thayDoiFontChu(fonts[position])
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }
