package com.asia.viblo.view.custom

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import android.widget.TextView
import com.asia.viblo.R
import com.asia.viblo.model.ContentChild
import com.asia.viblo.model.TypeContent.*
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.utils.getUrlListFromString
import com.asia.viblo.utils.loadImageUrl
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
                data.contains("<ul>") || data.contains("<ol>") -> {
                    val content = data
                            .replace("<li>", "<p>&#160;&#160;●&#160;&#160;")
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
                    val content = data
                            .replace("<span class=\"hljs-string\">", "<font color = '#449173'>")
                            .replace("<span class=\"hljs-keyword\">", "<font color = '#c479db'>")
                            .replace("<span class=\"hljs-name\">", "<font color = '#c7504a'>")
                            .replace("<span class=\"hljs-attr\">", "<font color = '#cf9153'>")
                            .replace("<span class=\"hljs-class\">", "<font color = '#95b064'>")
                            .replace("<span class=\"hljs-title\">", "<font color = '#3683e0'>")//
                            .replace("<span class=\"hljs-meta\">", "<font color = '#3683e0'>")
                            .replace("<span class=\"hljs-selector-tag\">", "<font color = '#bd4147'>")
                            .replace("<span class=\"hljs-doctag\">", "<font color = '#bd4147'>")
                            .replace("<span class=\"hljs-comment\">", "<font color = '#bd4147'>")
                            .replace("<span class=\"hljs-function\">", "<font color = '#bd4147'>")
                            .replace("<span class=\"hljs-params\">", "<font color = '#bd4147'>")
                            .replace("<span class=\"hljs-number\">", "<font color = '#bd4147'>")
                            .replace("<span class=\"hljs-selector-pseudo\">", "<font color = '#bd4147'>")
                            .replace("<span class=\"hljs-type\">", "<font color = '#bd4147'>")
                            .replace("<span class=\"hljs-literal\">", "<font color = '#bd4147'>")
                            .replace("<span class=\"hljs-selector-class\">", "<font color = '#bd4147'>")
                            .replace("<span class=\"hljs-attribute\">", "<font color = '#bd4147'>")
                            .replace("<span class=\"hljs-tag\">", "<font color = '#bd4147'>")
                            .replace("</span>", "</font>")
                            .replace("<pre><code>", "")
                            .replace("</code></pre>", "")
                    contentChildList.add(ContentChild(content, CODE_CLASS))
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
//            Log.d("TAG", "content = " + contentChild.content)
            when (contentChild.typeContent) {
                TEXT, CODE, IMAGE -> {
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
//                    createTable(layoutInflater, contentChild.content)
                }
                CODE_CLASS -> {
                    val contentList = contentChild.content.split("\n").toMutableList()
                    val layout = layoutInflater.inflate(R.layout.item_layout_content_code, this, false)
                    for (content: String in contentList) {
                        layout.layoutCode.addView(createText(layoutInflater, content, R.color.colorCode))
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

    private fun createTable(layoutInflater: LayoutInflater, data: String) {
        val theadList: MutableList<String> = arrayListOf()
        val content = data
                .replace("\n", "")
                .replace("<tr>", "")
                .replace("</tr>", "")
        val thead = content.substring(content.indexOf("<thead>") + "<thead>".length,
                content.indexOf("</thead>"))
        val dataThead = thead.split("</th>")
        val theadSize = dataThead.size - 1
        for ((index, text) in dataThead.withIndex()) {
            if (index < theadSize) {
                theadList.add(text)
            }
        }
        val body = content.substring(content.indexOf("<tbody>") + "<tbody>".length,
                content.indexOf("</tbody>"))
        val dataBody = body.split("</td>")
        val bodyList: MutableList<MutableList<String>> = arrayListOf()
        val childList: MutableList<String> = arrayListOf()
        val bodySize = dataBody.size - 1
        for ((index, text) in dataBody.withIndex()) {
            if (index < bodySize) {
                if (index > 0 && index % theadSize == 0) {
                    bodyList.add(childList.toMutableList())
                    childList.clear()
                }
                childList.add(text.replace("<td>", "").trim())
            }
        }
        val table = layoutInflater.inflate(R.layout.item_table_content_default, this, false) as TableLayout
        for (theadData: String in theadList) {
            Log.d("TAG", "theadData = " + theadData)
        }
        for (bodyDataList: MutableList<String> in bodyList) {
            val tableRow = layoutInflater.inflate(R.layout.item_table_row_content_default, table, false) as TableRow
            val layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
            tableRow.layoutParams = layoutParams
            for (bodyData: String in bodyDataList) {
                Log.d("TAG", "bodyData = " + bodyData)
                tableRow.addView(createText(layoutInflater, bodyData, null, R.layout.item_text_content_table))
            }
            table.addView(tableRow)
        }
        addView(table)
    }

    private fun createImage(layoutInflater: LayoutInflater, url: String): ImageView {
        val imageView = layoutInflater.inflate(
                R.layout.item_image_content_default, this, false) as ImageView
        loadImageUrl(imageView, url)
        return imageView
    }

    private fun createText(layoutInflater: LayoutInflater, text: String): TextView {
        return createText(layoutInflater, text, null)
    }

    private fun createText(layoutInflater: LayoutInflater, text: String, idColor: Int?): TextView {
        return createText(layoutInflater, text, idColor, R.layout.item_text_content_default)
    }

    private fun createText(layoutInflater: LayoutInflater, text: String, idColor: Int?, idLayout: Int): TextView {
        val textView = layoutInflater.inflate(idLayout, this, false) as TextView
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
