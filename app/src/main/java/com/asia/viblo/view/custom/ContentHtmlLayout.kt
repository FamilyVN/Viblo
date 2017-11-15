package com.asia.viblo.view.custom

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.asia.viblo.R
import com.asia.viblo.model.ContentChild
import com.asia.viblo.model.TypeContent.*
import com.asia.viblo.utils.getUrlListFromString
import com.asia.viblo.utils.loadAvatar
import kotlinx.android.synthetic.main.item_layout_content_code.view.*
import kotlinx.android.synthetic.main.item_layout_content_default.view.*

/**
 * Created by anhtv on 14/11/2017.
 */
class ContentHtmlLayout : LinearLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        orientation = VERTICAL
    }

    fun addContentHtml(contentHtml: MutableList<String>) {
        val contentChildList: MutableList<ContentChild> = arrayListOf()
        for (data: String in contentHtml) {
            when {
                data.contains("<ul>") -> {
                    contentChildList.add(ContentChild(data))
                }
                data.contains("<img class=\"block-image\" src=") -> {
                    contentChildList.add(ContentChild(data, IMAGE))
                }
                data.contains("<table>") -> {
                    contentChildList.add(ContentChild(data, TABLE))
                }
                data.contains("<blockquote>") -> {
                    val content = data
                            .replace("<blockquote>", "")
                            .replace("</blockquote>", "")
                    contentChildList.add(ContentChild(content, BLOCK_QUOTE))
                }
                data.contains("<pre><code") -> {
                    contentChildList.add(ContentChild(data, CODE_CLASS))
                }
                data.contains("<code>") -> {
                    val content = data
                            .replace("<code>", "<font color = '#bd4147'>")
                            .replace("</code>", "</font>")
                    contentChildList.add(ContentChild(content, CODE))
                }
                else -> {
                    if (!TextUtils.isEmpty(data) && data.isNotBlank()) {
                        contentChildList.add(ContentChild(data))
                    }
                }
            }
        }
        val layoutInflater = LayoutInflater.from(context)
        for (contentChild: ContentChild in contentChildList) {
            Log.d("TAG", "content = " + contentChild.content)
            when (contentChild.typeContent) {
                TEXT, CODE -> {
                    val textView = layoutInflater.inflate(
                            R.layout.item_text_content_default, this, false) as TextView
                    textView.setText(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        Html.fromHtml(contentChild.content, Html.FROM_HTML_MODE_COMPACT) else
                        Html.fromHtml(contentChild.content), TextView.BufferType.SPANNABLE)
                    addView(textView)
                }
                IMAGE -> {
                    val list = contentChild.content.split("\"")
                    for (sub: String in list) {
                        val urlList = getUrlListFromString(sub)
                        for (url in urlList) {
                            val imageView = layoutInflater.inflate(
                                    R.layout.item_image_content_default, this, false) as ImageView
                            loadAvatar(imageView, url)
                            addView(imageView)
                        }
                    }
                }
                TABLE -> {
                }
                CODE_CLASS -> {
                    val contentList = contentChild.content.split("\n").toMutableList()
                    val layout = layoutInflater.inflate(R.layout.item_layout_content_code, this, false)
                    for (content: String in contentList) {
                        val textView = layoutInflater.inflate(
                                R.layout.item_text_content_default, this, false) as TextView
                        textView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
                        textView.setText(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT) else
                            Html.fromHtml(content), TextView.BufferType.SPANNABLE)
                        layout.layoutCode.addView(textView)
                    }

                    addView(layout)
                }
                BLOCK_QUOTE -> {
                    val layout = layoutInflater.inflate(
                            R.layout.item_layout_content_default, this, false) as LinearLayout
                    layout.txtContent.setText(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        Html.fromHtml(contentChild.content, Html.FROM_HTML_MODE_COMPACT) else
                        Html.fromHtml(contentChild.content), TextView.BufferType.SPANNABLE)
                    addView(layout)
                }
            }
        }
    }
}
