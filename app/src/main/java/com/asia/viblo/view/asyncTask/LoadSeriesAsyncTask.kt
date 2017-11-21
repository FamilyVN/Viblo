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
private val cssQuerySeriesDetail = "div#__nuxt > div#app-container > div#main-content > " +
        "div.series-view > div > div.row > div.col-xl-9 > div.series-content-wrapper"
private val cssQuerySeriesAvatar = "div.content-author > a > img"
private val cssQuerySeriesName = "div.content-author > div.mw-0 > div.text-bold > div.overflow-hidden > a"
private val cssQuerySeriesPublished = "div.content-author > div.mw-0 > div.text-muted"
private val cssQuerySeriesTitle = "div.my-05 > header.summary > h1"
private val cssQuerySeriesTag = "div.my-05 > header.summary > div.tags > a"
private val cssQuerySeriesScore = "div.my-05 > div.votes > div.points"
private val cssQuerySeriesStatus = "div.my-05 > header.summary > div.d-flex > div.d-block > " +
        "ul.post-right-menu > li.hidden-lg-up > span"
private val cssQuerySeriesData = "div.md-contents"
private val cssQuerySeriesContent = "div > div.card"
private val cssQuerySeriesContentAvatar = "div.card-block > figure.post-author-avatar > a > img"
private val cssQuerySeriesContentName = "div.card-block > div.ml-05 > div.post-header > div.post-meta > a"
private val cssQuerySeriesContentTime = "div.card-block > div.ml-05 > div.post-header > " +
        "div.post-meta > div.text-muted > span"
private val cssQuerySeriesContentPostUrl = "div.card-block > div.ml-05 > div.post-header > " +
        "div.post-title-box > h1 > a"
private val cssQuerySeriesContentAuthorUrl = "div.card-block > figure.post-author-avatar"
private val cssQuerySeriesContentScore = "div.card-block > div.ml-05 > div.d-flex > div.points > span"
private val cssQuerySeriesContentStatus = "div.card-block > div.ml-05 > div.d-flex > div.d-flex > " +
        "div.post-stats"
private val cssQuerySeriesContentTag = "div.card-block > div.ml-05 > div.post-header > " +
        "div.post-title-box > div.tags > a"

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
            seriesDetail.score = element.select(cssQuerySeriesScore).text()
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
        val postList: MutableList<Post> = arrayListOf()
        try {
            for (content: Element in contentSubject!!) {
                val post = Post()
//                val avatarSubject = element.select(cssQuerySeriesContentAvatar).attr("srcset")!!.split(",")
                val avatarSubject = element.select(cssQuerySeriesContentAvatar).first()
                val nameSubject = element.select(cssQuerySeriesContentName).first()
                val timeSubject = element.select(cssQuerySeriesContentTime).first()
                val urlSubject = element.select(cssQuerySeriesContentPostUrl).first()
                val authorsUrlSubject = element.select(cssQuerySeriesContentAuthorUrl).first()
                val scoreSubject = element.select(cssQuerySeriesContentScore).first()
                val statusSubject = element.select(cssQuerySeriesContentStatus).first()
                val tagSubject = element.select(cssQuerySeriesContentTag)
                if (statusSubject != null) {
                    val viewSubject = statusSubject.getElementsByTag("span")
                    viewSubject.map {
                        if (TextUtils.isEmpty(it.text())) "0" else it.text()
                    }
                            .forEachIndexed { index, data ->
                                when (index) {
                                    0 -> post.views = data
                                    1 -> post.clips = data
                                    2 -> post.comments = data
                                }
                            }
                }
//                val avatar = avatarSubject[avatarSubject.size - 1]
//                post.avatar = avatar.substring(0, avatar.length - 3)
                post.avatar = avatarSubject?.attr("src")!!
                post.name = nameSubject?.text()!!
                post.time = timeSubject?.text()!!
                post.postUrl = urlSubject?.attr("href")!!
                post.authorUrl = authorsUrlSubject?.getElementsByTag("a")?.first()?.attr("href")!!
                post.title = urlSubject.text()!!
                if (scoreSubject != null) {
                    post.score = scoreSubject.text()
                }
                tagSubject
                        .filterNot { TextUtils.isEmpty(it.text()) }
                        .forEach {
                            post.tags.add(it.text())
                            post.tagUrlList.add(it.attr("href"))
                        }
                postList.add(post)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        seriesDetail.seriesList.addAll(postList)
    }

    override fun onPostExecute(result: SeriesDetail?) {
        super.onPostExecute(result)
        mOnUpdateSeriesDetail.onUpdateSeriesDetail(result)
    }
}