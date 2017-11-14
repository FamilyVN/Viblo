package com.asia.viblo.view.custom

import android.content.Context
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
import com.asia.viblo.model.TypeContent
import com.asia.viblo.utils.getUrlListFromString
import com.asia.viblo.utils.loadAvatar

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
                    contentChildList.add(ContentChild(data, TypeContent.IMAGE))
                }
                data.contains("<table>") -> {
                    contentChildList.add(ContentChild(data, TypeContent.TABLE))
                }
                data.contains("<pre><code class=") -> {
                    contentChildList.add(ContentChild(data, TypeContent.CODE))
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
                TypeContent.TEXT -> {
                    val textView = layoutInflater.inflate(
                            R.layout.item_text_content_default, this, false) as TextView
                    textView.text = Html.fromHtml(contentChild.content)
                    addView(textView)
                }
                TypeContent.IMAGE -> {
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
                TypeContent.TABLE -> {
                }
                TypeContent.CODE -> {
                }
            }
        }
    }
}
