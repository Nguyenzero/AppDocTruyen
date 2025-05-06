package com.example.doctruyen

import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.graphics.Color

fun TextView.setExpandableText(fullText: String, maxLines: Int = 3) {
    val seeMore = "... Đọc tiếp"
    val seeLess = " Thu gọn"

    post {
        if (lineCount > maxLines) {
            this.maxLines = maxLines
            this.ellipsize = TextUtils.TruncateAt.END

            val end = layout?.getLineEnd(maxLines - 1) ?: fullText.length
            val truncated = fullText.substring(0, end).trim()

            val spannable = SpannableString("$truncated$seeMore")
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    this@setExpandableText.setCollapsibleText(fullText, seeLess)
                }
            }
            spannable.setSpan(clickableSpan, spannable.length - seeMore.length, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            text = spannable
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        } else {
            text = fullText
        }
    }
}

private fun TextView.setCollapsibleText(fullText: String, seeLessText: String) {
    val spannable = SpannableString("$fullText$seeLessText")
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            this@setCollapsibleText.setExpandableText(fullText)
        }
    }
    spannable.setSpan(clickableSpan, spannable.length - seeLessText.length, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    text = spannable
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
}
