package com.asia.viblo.view.custom

import android.content.Context
import android.graphics.Typeface
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
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

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
        val contentChildList = setupContentChild(contentHtml)
        createView(contentChildList)
    }

    private fun setupContentChild(contentHtml: MutableList<String>): MutableList<ContentChild> {
        val contentChildList: MutableList<ContentChild> = arrayListOf()
        for (data: String in contentHtml) {
            when {
                data.contains("<ul>") || data.contains("<ol>") -> {
                    val content = data
                            .replace("<li><p>", "<li>")
                            .replace("<li>", "<li>&#160;&#160;●&#160;&#160;")
                            .replace("</p></li>", "</li>")
                            .replace("</li>", "</li>")
                            .replace("<code>", "<font color = '#bd4147'>")
                            .replace("</code>", "</font>")
                            .replace("<ul>", "")
                            .replace("</ul>", "")
                            .replace("<ol>", "")
                            .replace("</ol>", "")
                            .trim()
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
                            .replace("<p>", "")
                            .replace("</p>", "")
                    contentChildList.add(ContentChild(content, BLOCK_QUOTE))
                }
                data.contains("<pre><code") -> {
                    val content = data
                            .replace("<span class=\"hljs-string\">", "<font color = '#98c379'>")
                            .replace("<span class=\"hljs-keyword\">", "<font color = '#c678dd'>")
                            .replace("<span class=\"hljs-name\">", "<font color = '#c7504a'>")
                            .replace("<span class=\"hljs-attr\">", "<font color = '#cf9153'>")
                            .replace("<span class=\"hljs-class\">", "<font color = '#d19a66'>")
                            .replace("<span class=\"hljs-title\">", "<font color = '#e6c07b'>")//
                            .replace("<span class=\"hljs-meta\">", "<font color = '#61aeee'>")
                            .replace("<span class=\"hljs-selector-tag\">", "<font color = '#bd4147'>")
                            .replace("<span class=\"hljs-doctag\">", "<font color = '#bd4147'>")
                            .replace("<span class=\"hljs-function\">", "")
                            .replace("<span class=\"hljs-params\">", "<font color = '#abb2bf'>")
                            .replace("<span class=\"hljs-number\">", "<font color = '#c98e53'>")
                            .replace("<span class=\"hljs-selector-pseudo\">", "<font color = '#d19a66'>")
                            .replace("<span class=\"hljs-type\">", "<font color = '#d19a66'>")
                            .replace("<span class=\"hljs-literal\">", "<font color = '#56b6c2'>")
                            .replace("<span class=\"hljs-selector-class\">", "<font color = '#d19a66'>")
                            .replace("<span class=\"hljs-attribute\">", "<font color = '#98c379'>")
                            .replace("<span class=\"hljs-selector-attr\">", "<font color = '#d19a66'>")
                            .replace("<span class=\"hljs-tag\">", "<font color = '#abb2bf'>")
                            .replace("<span class=\"hljs-built_in\">", "<font color = '#e6c07b'>")
                            .replace("<code class=\"hljs language-javascript\">", "")
                            .replace("<span class=\"xml\">", "")
                            .replace("</span>", "</font>")
                            .replace("<pre><code>", "")
                            .replace("</code></pre>", "")
                            .replace("<pre>", "")
                            .replace("</pre>", "")
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
        return contentChildList
    }

    private fun createView(contentChildList: MutableList<ContentChild>) {
        val layoutInflater = LayoutInflater.from(context)
        for (contentChild: ContentChild in contentChildList) {
            when (contentChild.typeContent) {
                TEXT, CODE, IMAGE -> {
                    val listBr = contentChild.content.split("<br>")
                    for (text: String in listBr) {
                        when {
                            text.contains("<img ") -> {
                                val list = text.split("\"")
                                for (sub: String in list) {
                                    val urlList = getUrlListFromString(sub)
                                    for (url in urlList) {
                                        addView(createImage(layoutInflater, url))
                                    }
                                }
                            }
                            text.contains("<code>") -> {
                                val content = text
                                        .replace("<code>", "<font color = '#bd4147'>")
                                        .replace("</code>", "</font>")
                                addView(createText(layoutInflater, content))
                            }
                            text.contains("<li>") -> {
                                val listLi = text.split("</li>")
                                for (li: String in listLi) {
                                    if (!TextUtils.isEmpty(li)) {
                                        addView(createText(layoutInflater, li))
                                    }
                                }
                            }
                            text.contains("<h1>")
                                    || text.contains("<h2>")
                                    || text.contains("<h3>")
                                    || text.contains("<h4>") -> {
                                val child = text
                                        .replace("<h1>", "")
                                        .replace("</h1>", "")
                                        .replace("<h2>", "")
                                        .replace("</h2>", "")
                                        .replace("<h3>", "")
                                        .replace("</h3>", "")
                                        .replace("<h4>", "")
                                        .replace("</h4>", "")
                                val textView = createText(layoutInflater, child)
                                textView.textSize =
                                        resources.getDimension(R.dimen.text_size_8)
                                textView.typeface = Typeface.DEFAULT_BOLD
                                addView(textView)
                            }
                            else -> {
                                val child = text
                                        .replace("<p>", "")
                                        .replace("</p>", "")
                                addView(createText(layoutInflater, child))
                            }
                        }
                    }
                }
                TABLE -> {
                    createTable(layoutInflater, contentChild.content)
                }
                CODE_CLASS -> {
                    val contentList = contentChild.content.split("\n").toMutableList()
                    val layout = layoutInflater.inflate(R.layout.item_layout_content_code, this, false)
                    for (content: String in contentList) {
                        var text = content
                        if (content.contains("<span class=\"hljs-comment\">")) {
                            text = text
                                    .replace("<span class=\"hljs-comment\">"
                                            , ("<font color = '#5c6370'>"))
//                                    .replace("/*", "/*●\n")
//                                    .replace("//", "\n//")
//                                    .replace("{ ", "{ ●\n")
//                                    .replace("; ", "; ●\n")
//                                    .replace("} ", "} ●\n")
//                                    .replace("*/", "*/●")
//                            Log.d("TAG", "text = " + text)
                            // todo 22/11/2017 fix later
                        }
                        text = changeSpaceString(text)
                        val childList = text.split("\n").toMutableList()
                        for (child: String in childList) {
                            var childText = if (child.contains("●")) {
                                "<font color = '#5c6370'>&#160;&#160;&#160;"
                            } else {
                                "&#160;"
                            } + child.replace("●", "&#160;")
                            childText = changeSpaceString(childText)
//                            Log.d("TAG", "childText = " + childText)
                            layout.layoutCode.addView(createText(layoutInflater, childText, R.color.colorNote))
                        }
                    }
                    addView(layout)
                }
                BLOCK_QUOTE -> {
                    val layout = layoutInflater.inflate(
                            R.layout.item_layout_content_default, this, false) as LinearLayout
                    layout.txtContent.setText(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        Html.fromHtml(contentChild.content, Html.FROM_HTML_MODE_COMPACT) else
                        Html.fromHtml(contentChild.content), TextView.BufferType.SPANNABLE)
                    setupOpenUrl(layout.txtContent, contentChild.content)
                    addView(layout)
                }
            }
        }
    }

    private fun changeSpaceString(text: String): String {
        var string = text
        val index = text.split("<font color = ")[0].length
        if (index >= 0) {
            var space = text.substring(0, index)
            space = space.replace(" ", "&#160;")
            string = space + text.substring(index, text.length)
        }
        return string
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
        setupOpenUrl(textView, text)
        return textView
    }

    private fun setupOpenUrl(textView: TextView, text: String) {
        val urlList = getUrlListFromString(text)
        if (urlList.size > 0) {
            textView.setOnClickListener {
                if (urlList[0].contains(baseUrlViblo)) {
                    Toast.makeText(context, urlList[0], Toast.LENGTH_SHORT).show()
                } else {
                    openBrowser(context, urlList[0])
                }
            }
        } else {
            if (text.contains("</a>")) {
                val html = text.substring(text.indexOf("<a"), text.indexOf("</a>") + "</a>".length)
                val element = Jsoup.parse(html, "", Parser.xmlParser())
                val url = element?.select("a")?.attr("href")
                if (url != null) {
                    textView.setOnClickListener {
                        Toast.makeText(context, baseUrlViblo + url, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
