    package com.example.doctruyen.doctruyen

    import android.os.Bundle
    import android.speech.tts.TextToSpeech
    import android.speech.tts.UtteranceProgressListener
    import android.util.Log
    import android.widget.*
    import androidx.appcompat.app.AlertDialog
    import androidx.appcompat.app.AppCompatActivity
    import com.example.doctruyen.R
    import com.example.doctruyen.database.AppDatabase
    import com.example.doctruyen.entity.Chapter
    import com.google.android.material.bottomsheet.BottomSheetDialog
    import kotlinx.coroutines.*
    import java.util.*

    class ListenStoryActivity : AppCompatActivity() {

        private lateinit var textToSpeech: TextToSpeech
        private lateinit var tvContent: TextView
        private lateinit var btnPlayPause: ImageButton
        private lateinit var tvStoryTitle: TextView
        private lateinit var tvChapterTitle: TextView

        private var isSpeaking = false
        private var currentIndex = 0
        private var contentList: List<String> = listOf()
        private var currentText = ""

        private lateinit var chapterList: List<Chapter>
        private var currentChapterPos = -1
        private var autoPlay = true
        private lateinit var seekBar: SeekBar
        private lateinit var tvProgress: TextView
        private lateinit var speedSeekBar: SeekBar
        private lateinit var voiceSpinner: Spinner
        private var speechRate = 1.0f
        private var stopSpeakingJob: Job? = null




        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.listen_story_layout)

            // Ánh xạ view
            tvContent = findViewById(R.id.tvContent)
            tvStoryTitle = findViewById(R.id.tvStoryTitle)
            tvChapterTitle = findViewById(R.id.tvChapterTitle)
            btnPlayPause = findViewById(R.id.btnPlayPause)
            val btnBack = findViewById<ImageButton>(R.id.btnBack)
            val btnPrev = findViewById<ImageButton>(R.id.btnPrev)
            val btnNext = findViewById<ImageButton>(R.id.btnNext)
            autoPlay = intent.getBooleanExtra("autoPlay", true)


            val timerLayout = findViewById<LinearLayout>(R.id.timerLayout)
            timerLayout.setOnClickListener {
                showTimerDialog()
            }

            val btnChapterList = findViewById<Button>(R.id.btnChapterList)
            btnChapterList.setOnClickListener {
                showChapterListDialog()
            }



            btnBack.setOnClickListener { finish() }


            seekBar = findViewById(R.id.seekBar)
            tvProgress = findViewById(R.id.tvProgress)


            val btnSettings = findViewById<ImageButton>(R.id.btnSettings)

            btnSettings.setOnClickListener {
                showSettingsBottomSheet()
            }



            seekBar.max = 100
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser && contentList.isNotEmpty()) {
                        val newIndex = (progress * contentList.size) / 100
                        if (newIndex in contentList.indices) {
                            currentIndex = newIndex
                            tvContent.text = contentList[currentIndex]
                            val charsRead = contentList.take(currentIndex + 1).sumOf { it.length }
                            val totalChars = currentText.length
                            val progressPercent = (charsRead * 100) / totalChars
                            seekBar?.let {
                                it.progress = progressPercent
                            }
                            tvProgress.text = "$progressPercent%"

                            if (isSpeaking) {
                                speakText(contentList[currentIndex])
                            }
                        }
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            // Khởi tạo TextToSpeech
            textToSpeech = TextToSpeech(this) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.language = Locale("vi", "VN")
                    setTTSListener()
                } else {
                    Toast.makeText(this@ListenStoryActivity, "Không thể khởi tạo TTS", Toast.LENGTH_SHORT).show()
                }
            }

            // Nhận dữ liệu từ Intent
            val storyId = intent.getIntExtra("storyId", -1)
            val chapterId = intent.getIntExtra("currentChapterIndex", -1)

            // Load dữ liệu
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(this@ListenStoryActivity)
                val story = db.storyDao().getStoryById(storyId)
                val chapter = db.chapterDao().getChapterById(chapterId)
                chapterList = db.chapterDao().getChaptersByStoryId(storyId)
                currentChapterPos = chapterList.indexOfFirst { it.chapterNumber == chapter?.chapterNumber }

                withContext(Dispatchers.Main) {
                    if (story != null && chapter != null) {
                        tvStoryTitle.text = story.title
                        tvChapterTitle.text = chapter.title
                        loadChapter(chapter)
                    } else {
                        Toast.makeText(this@ListenStoryActivity, "Không tìm thấy truyện hoặc chương", Toast.LENGTH_SHORT).show()
                    }

                    btnPrev.setOnClickListener {
                        if (currentChapterPos > 0) {
                            currentChapterPos--
                            loadChapter(chapterList[currentChapterPos])
                        } else {
                            Toast.makeText(this@ListenStoryActivity, "Đây là chương đầu tiên", Toast.LENGTH_SHORT).show()
                        }
                    }

                    btnNext.setOnClickListener {
                        if (currentChapterPos < chapterList.size - 1) {
                            currentChapterPos++
                            loadChapter(chapterList[currentChapterPos])
                        } else {
                            Toast.makeText(this@ListenStoryActivity, "Đây là chương cuối cùng", Toast.LENGTH_SHORT).show()
                        }
                    }

                    btnPlayPause.setOnClickListener {
                        if (isSpeaking) {
                            stopSpeech()
                        } else {
                            speakText(contentList[currentIndex])
                        }
                    }
                }
            }
        }

        private fun showSettingsBottomSheet() {
            val view = layoutInflater.inflate(R.layout.bottom_sheet_setting_listen, null)
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(view)


            // Gán các view vào biến lớp
            speedSeekBar = view.findViewById(R.id.speedSeekBar)
            voiceSpinner = view.findViewById(R.id.voiceSpinner)


            // Gán giá trị hiện tại cho thanh điều chỉnh tốc độ
            speedSeekBar.progress = (speechRate * 10).toInt()

            // Hiển thị tốc độ hiện tại
            val speedValueTextView = view.findViewById<TextView>(R.id.speedValueTextView)
            speedValueTextView.text = "${speechRate}x"

            speedSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val rate = progress / 10.0f
                    speechRate = rate
                    textToSpeech.setSpeechRate(rate)

                    // Cập nhật giá trị tốc độ hiển thị
                    speedValueTextView.text = "${String.format("%.1f", rate)}x"

                    // Nếu đang nói thì phát lại đoạn hiện tại với tốc độ mới
                    if (isSpeaking && currentIndex in contentList.indices) {
                        speakText(contentList[currentIndex])
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            // Thiết lập danh sách giọng đọc tiếng Việt
            val availableVoices = textToSpeech.voices
                .filter { it.locale.language == "vi" }
                .toList()

            val voiceNames = availableVoices.map { it.name }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, voiceNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            voiceSpinner.adapter = adapter

            voiceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                    val selectedVoice = availableVoices[position]
                    textToSpeech.voice = selectedVoice

                    // Nếu đang nói thì phát lại đoạn hiện tại với giọng mới
                    if (isSpeaking && currentIndex in contentList.indices) {
                        speakText(contentList[currentIndex])
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            // Chọn đúng giọng hiện tại nếu có
            textToSpeech.voice?.let {
                val index = voiceNames.indexOf(it.name)
                if (index >= 0) voiceSpinner.setSelection(index)
            }

            dialog.show()
        }


        private fun showChapterListDialog() {
            val chapterTitles = chapterList.map { "Chương ${it.chapterNumber}: ${it.title}" }

            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Danh sách chương")
            builder.setItems(chapterTitles.toTypedArray()) { _, which ->
                currentChapterPos = which
                loadChapter(chapterList[which])
            }
            builder.setNegativeButton("Đóng", null)
            builder.show()
        }

        private fun showTimerDialog() {
            val times = arrayOf("5 phút", "10 phút", "15 phút", "30 phút", "Hủy hẹn giờ")
            val timeValues = arrayOf(5, 10, 15, 30)

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Chọn thời gian hẹn giờ")
            builder.setItems(times) { _, which ->
                if (which in timeValues.indices) {
                    val minutes = timeValues[which]
                    startTimerToStopSpeaking(minutes)
                    Toast.makeText(this, "Đã hẹn giờ tắt sau $minutes phút", Toast.LENGTH_SHORT).show()
                } else {
                    cancelTimer()
                    Toast.makeText(this, "Đã hủy hẹn giờ", Toast.LENGTH_SHORT).show()
                }
            }
            builder.show()
        }


        private fun startTimerToStopSpeaking(minutes: Int) {
            stopSpeakingJob?.cancel()  // Hủy timer cũ nếu có
            stopSpeakingJob = CoroutineScope(Dispatchers.Main).launch {
                delay(minutes * 60 * 1000L)  // Delay theo phút
                stopSpeech()  // Gọi hàm dừng đọc truyện
                Toast.makeText(this@ListenStoryActivity, "Đã đến giờ, đã dừng đọc", Toast.LENGTH_SHORT).show()
            }
        }

        private fun cancelTimer() {
            stopSpeakingJob?.cancel()
            stopSpeakingJob = null
        }


        private fun setTTSListener() {
            textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onError(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    if (currentIndex < contentList.size - 1) {
                        currentIndex++
                        runOnUiThread {
                            val charsRead = contentList.take(currentIndex + 1).sumOf { it.length }
                            val totalChars = currentText.length
                            val progressPercent = (charsRead * 100) / totalChars
                            seekBar.progress = progressPercent
                            tvProgress.text = "$progressPercent%"

                            tvContent.text = contentList[currentIndex]
                            speakText(contentList[currentIndex])

                        }
                    } else {
                        runOnUiThread {
                            if (currentChapterPos < chapterList.size - 1) {
                                speakFinalMessage("Hết chương, chuẩn bị qua chương mới...") {
                                    currentChapterPos++
                                    loadChapter(chapterList[currentChapterPos])
                                    speakText(contentList[currentIndex]) // tự động phát chương mới
                                }
                            } else {
                                speakFinalMessage("Đây là chương cuối cùng. Cảm ơn bạn đã nghe truyện.") {
                                    btnPlayPause.setImageResource(R.drawable.play)
                                    isSpeaking = false
                                }
                            }
                        }
                    }
                }
            })
        }

        private fun speakFinalMessage(message: String, onComplete: () -> Unit) {
            val finalParams = HashMap<String, String>()
            finalParams[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "finalMessage"

            textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onError(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    runOnUiThread {
                        onComplete()
                        setTTSListener() // Gán lại listener chính
                    }
                }
            })

            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, finalParams)
        }

        private fun loadChapter(chapter: Chapter) {
            stopSpeech()
            currentText = chapter.content
            contentList = splitContentIntoChunks(currentText, 300)
            currentIndex = 0
            tvContent.text = contentList[currentIndex]
            val chapterTitle = "Chương ${chapter.chapterNumber}: ${chapter.title}"
            tvChapterTitle.text = chapterTitle




            if (autoPlay) {

                speakText(contentList[currentIndex])
                seekBar.progress = 0
                tvProgress.text = "0%"

            } else {
                btnPlayPause.setImageResource(R.drawable.play)
                isSpeaking = false
            }
        }

        private fun speakText(text: String) {
            if (text.isNotEmpty() && ::textToSpeech.isInitialized) {
                val params = HashMap<String, String>()
                params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "storySpeech"
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params)
                btnPlayPause.setImageResource(R.drawable.pause)
                isSpeaking = true
            } else {
                Log.e("TextToSpeech", "TTS chưa được khởi tạo hoặc không có nội dung để phát")
            }
        }

        private fun stopSpeech() {
            textToSpeech.stop()
            isSpeaking = false
            btnPlayPause.setImageResource(R.drawable.play)  // Đặt lại trạng thái nút phát
        }

        private fun splitContentIntoChunks(content: String, maxLength: Int): List<String> {
            val result = mutableListOf<String>()
            var remainingText = content.trim()

            while (remainingText.isNotEmpty()) {
                if (remainingText.length <= maxLength) {
                    result.add(remainingText)
                    break
                }

                val endIndex = listOf('.', '!', '?')
                    .map { remainingText.lastIndexOf(it, maxLength) }
                    .maxOrNull()
                    ?.takeIf { it >= 0 }

                val splitIndex = endIndex ?: remainingText.lastIndexOf(' ', maxLength).takeIf { it >= 0 } ?: maxLength

                val chunk = remainingText.substring(0, splitIndex + 1).trim()
                result.add(chunk)
                remainingText = remainingText.substring(splitIndex + 1).trim()
            }

            return result
        }

        override fun onDestroy() {
            stopSpeakingJob?.cancel()
            if (::textToSpeech.isInitialized) {
                textToSpeech.stop()
                textToSpeech.shutdown()
            }
            super.onDestroy()
        }

    }
