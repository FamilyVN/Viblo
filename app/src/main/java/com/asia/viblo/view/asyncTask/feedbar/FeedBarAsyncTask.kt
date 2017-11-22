package com.asia.viblo.view.asyncTask.feedbar

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.asia.viblo.view.asyncTask.TypeQuery
import com.asia.viblo.view.fragment.OnUpdateFeedBar
import org.jsoup.Jsoup

/**
 * Created by FRAMGIA\vu.tuan.anh on 06/11/2017.
 */
private val cssQueryFeedBars = "div#__nuxt > div#app-container > div#main-content > " +
        "div > div.bg-inverse > div.container > div.feed-bar > div.feedbar-wrapper > ul.feed-links"
private val cssQueryAuthorFeedBars = "div#__nuxt > div#app-container > div#main-content > " +
        "div.user-page > div.profile-tabs > ul.container"
private val cssQueryFeedBar = "li.feedbar-item > a"
private val cssQueryAuthorFeedBar = "li.nav-item > a"

@SuppressLint("StaticFieldLeak")
class FeedBarAsyncTask(onUpdateFeedBar: OnUpdateFeedBar) : AsyncTask<String, Void, List<String>>() {
    private val mOnUpdateFeedBar: OnUpdateFeedBar = onUpdateFeedBar
    override fun doInBackground(vararg params: String?): List<String> {
        val feedBarList: MutableList<String> = arrayListOf()
        val baseUrl = params[0]
        try {
            val document = Jsoup.connect(baseUrl).get()
            val body = document.body()
            val data = body.select(getCssQuery(params, TypeQuery.FEED_BARS)).first()
            data?.select(getCssQuery(params, TypeQuery.FEED_BAR))?.mapTo(feedBarList) { it.text() }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return feedBarList
    }

    override fun onPostExecute(result: List<String>?) {
        super.onPostExecute(result)
        mOnUpdateFeedBar.onUpdateFeedBar(result)
    }

    private fun getCssQuery(params: Array<out String?>, typeQuery: TypeQuery): String {
        return if (params.size > 1) {
            when (typeQuery) {
                TypeQuery.FEED_BARS -> cssQueryAuthorFeedBars
                else -> cssQueryAuthorFeedBar
            }
        } else {
            when (typeQuery) {
                TypeQuery.FEED_BARS -> cssQueryFeedBars
                else -> cssQueryFeedBar
            }
        }
    }
}