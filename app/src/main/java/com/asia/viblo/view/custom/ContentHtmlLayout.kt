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
import android.widget.Toast
import com.asia.viblo.R
import com.asia.viblo.model.ContentChild
import com.asia.viblo.model.TypeContent.*
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.utils.getUrlListFromString
import com.asia.viblo.utils.loadAvatar
import com.asia.viblo.utils.openBrowser
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
            Log.d("TAG", "data = " + data)
            when {
                data.contains("<ul>") -> {
                    val content = data
                            .replace("<li>", "<p>&#160;&#160;●&#160&#160")
                            .replace("</li>", "</p>")
                            .replace("<code>", "<font color = '#bd4147'>")
                            .replace("</code>", "</font>")
                    contentChildList.add(ContentChild(content))
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
                    if (!TextUtils.isEmpty(data) && !TextUtils.equals(data, " ")) {
                        contentChildList.add(ContentChild(data))
                    }
                }
            }
        }
        val layoutInflater = LayoutInflater.from(context)
        for (contentChild: ContentChild in contentChildList) {
            when (contentChild.typeContent) {
                TEXT, CODE -> {
                    addView(createText(layoutInflater, contentChild.content))
                }
                IMAGE -> {
                    val listBr = contentChild.content.split("<br>")
                    for (text: String in listBr) {
                        if (text.contains("<img ")) {
                            val list = text.split("\"")
                            for (sub: String in list) {
                                val urlList = getUrlListFromString(sub)
                                for (url in urlList) {
                                    addView(createImage(layoutInflater, url))
                                }
                            }
                        } else {
                            addView(createText(layoutInflater, text))
                        }
                    }

                }
                TABLE -> {
                    val layout = layoutInflater.inflate(R.layout.item_layout_content_code, this, false)
                    addView(layout)
                }
                CODE_CLASS -> {
                    val contentList = contentChild.content.split("\n").toMutableList()
                    val layout = layoutInflater.inflate(R.layout.item_layout_content_code, this, false)
                    for (content: String in contentList) {
                        val text = content.replace(" ", "&#160;")
                        layout.layoutCode.addView(createText(layoutInflater, text, android.R.color.white))
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

    private fun createImage(layoutInflater: LayoutInflater, url: String): ImageView {
        val imageView = layoutInflater.inflate(
                R.layout.item_image_content_default, this, false) as ImageView
        loadAvatar(imageView, url)
        return imageView
    }

    private fun createText(layoutInflater: LayoutInflater, text: String): TextView {
        return createText(layoutInflater, text, null)
    }

    private fun createText(layoutInflater: LayoutInflater, text: String, idColor: Int?): TextView {
        val textView = layoutInflater.inflate(
                R.layout.item_text_content_default, this, false) as TextView
        textView.setText(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT) else
            Html.fromHtml(text), TextView.BufferType.SPANNABLE)
        if (idColor != null) {
            textView.setTextColor(ContextCompat.getColor(context, idColor))
        }

        val urlList = getUrlListFromString(text)
        if (urlList.size > 0) {
            textView.setOnClickListener {
                if (urlList[0].contains(baseUrlViblo)) {
                    Toast.makeText(context, urlList[0], Toast.LENGTH_SHORT).show()
                } else {
                    openBrowser(context, urlList[0])
                }
            }
        }
        return textView
    }
}
