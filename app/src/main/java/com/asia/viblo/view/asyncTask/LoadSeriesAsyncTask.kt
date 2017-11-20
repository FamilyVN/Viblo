package com.asia.viblo.view.asyncTask

import android.os.AsyncTask
import android.text.TextUtils
import com.asia.viblo.model.post.Post
import com.asia.viblo.model.series.SeriesDetail
import com.asia.viblo.view.activity.series.OnUpdateSeriesDetail
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * Created by TuanAnh on 11/19/2017.
 */
val cssQuerySeriesDetail = "div#__nuxt > div#app-container > div#main-content > " +
        "div.series-view > div > div.row > div.col-xl-9 > div.series-content-wrapper"
val cssQuerySeriesAvatar = "div.content-author > a > img"
val cssQuerySeriesName = "div.content-author > div.mw-0 > div.text-bold > div.overflow-hidden > a"
val cssQuerySeriesPublished = "div.content-author > div.mw-0 > div.text-muted"
val cssQuerySeriesTitle = "div.my-05 > header.summary > h1"
val cssQuerySeriesTag = "div.my-05 > header.summary > div.tags > a"
val cssQuerySeriesStatus = "div.my-05 > header.summary > div.d-flex > div.d-block >" +
        " ul.post-right-menu > li.hidden-lg-up > span"
val cssQuerySeriesData = "div.md-contents"
val cssQuerySeriesContent = "div > div.card"

class LoadSeriesAsyncTask(onUpdateSeriesDetail: OnUpdateSeriesDetail) : AsyncTask<String, Void, SeriesDetail>() {
    private val mOnUpdateSeriesDetail: OnUpdateSeriesDetail = onUpdateSeriesDetail
    override fun doInBackground(vararg params: String?): SeriesDetail {
        val seriesDetail = SeriesDetail()
        val baseUrl = params[0]
        try {
            val document = Jsoup.connect(baseUrl).get()
            val body = document.body()
            val element = body.select(cssQuerySeriesDetail)
            // header
            seriesDetail.avatar = element.select(cssQuerySeriesAvatar).attr("src")
            seriesDetail.name = element.select(cssQuerySeriesName).text()
            seriesDetail.publishingDate = element.select(cssQuerySeriesPublished).text()
            seriesDetail.title = element.select(cssQuerySeriesTitle).text()
            // tag
            val tagSubject = element.select(cssQuerySeriesTag)
            tagSubject
                    .filterNot { TextUtils.isEmpty(it.text()) }
                    .forEach {
                        seriesDetail.tags.add(it.text())
                        seriesDetail.tagUrlList.add(it.attr("href"))
                    }
            // status
            val statusSubject = element.select(cssQuerySeriesStatus)
            statusSubject?.map { it.text() }?.forEachIndexed { index, data ->
                when (index) {
                    0 -> seriesDetail.views = data
                    1 -> seriesDetail.clips = data
                    2 -> seriesDetail.posts = data
                }
            }
            // body
            val dataSubject = element.select(cssQuerySeriesData)
            dataSubject?.mapNotNull { it.childNodes() }?.flatMap { it }?.forEach {
                seriesDetail.data.add(it.outerHtml())
            }
            // contents
            loadContent(seriesDetail, element)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return seriesDetail
    }

    private fun loadContent(seriesDetail: SeriesDetail, element: Elements?) {
        val contentSubject = element?.select(cssQuerySeriesContent)
        for (content: Element in contentSubject!!) {
            val post = Post()

        }
    }

    override fun onPostExecute(result: SeriesDetail?) {
        super.onPostExecute(result)
        mOnUpdateSeriesDetail.onUpdateSeriesDetail(result)
    }
}