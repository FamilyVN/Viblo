package com.asia.viblo.view.custom

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.asia.viblo.R
import com.asia.viblo.model.ContentChild
import com.asia.viblo.model.TypeContent
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
        val builder = StringBuilder()
        for (data: String in contentHtml) {
            when {
                data.contains("<ul>") -> {
                    contentChildList.add(ContentChild(builder.toString()))
                    contentChildList.add(ContentChild(data))
                    builder.setLength(0)
                }
                data.contains("<img class=\"block-image\" src=") -> {
                    contentChildList.add(ContentChild(builder.toString()))
                    contentChildList.add(ContentChild(data, TypeContent.IMAGE))
                    builder.setLength(0)
                }
                else -> builder.append(data)
            }
        }
        contentChildList.add(ContentChild(builder.toString()))
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
                    val list = contentChild.content.split(" ")
                    if (list.size >= 2) {
                        val imageView = layoutInflater.inflate(
                                R.layout.item_image_content_default, this, false) as ImageView
                        val length = list[2].length
                        if (length > 5) {
                            val url = list[2].substring(5, length - 1)
                            loadAvatar(imageView, url)
                            addView(imageView)
                        }
                    }
                }
            }
        }
    }
}
