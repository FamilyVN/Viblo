package com.asia.viblo.view.asyncTask.webview

import android.os.AsyncTask
import com.asia.viblo.utils.getDocument
import com.asia.viblo.view.fragment.OnUpdateData
import org.jsoup.nodes.Document

/**
 * Created by FRAMGIA\vu.tuan.anh on 06/04/2018.
 */
private const val CSS_QUERY_BANNER = "div#__nuxt > div#__layout > div#app-container > div#main-content > div > a"
private const val CSS_QUERY_FOOTER = "div#__nuxt > div#__layout > div#app-container > footer.footer"
private const val CSS_QUERY_ASK_ON_VIBLO = "div#__nuxt > div#__layout > div#app-container > div#main-content > div > div > div.tag-promo"
private const val CSS_QUERY_HEADER = "div#__nuxt > div#__layout > div#app-container > nav#main-navbar"

class WebViewAsyncTask(onUpdateData: OnUpdateData<String>) : AsyncTask<String, Void, String>() {
    private val mOnUpdateData = onUpdateData
    override fun doInBackground(vararg params: String?): String {
        val document = getDocument(params[0]) as Document
        document.select(CSS_QUERY_BANNER).remove()
        document.select(CSS_QUERY_FOOTER).remove()
        document.select(CSS_QUERY_ASK_ON_VIBLO).remove()
        document.select(CSS_QUERY_HEADER).remove()
        return document.html()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        mOnUpdateData.onUpdateData(result)
    }
}