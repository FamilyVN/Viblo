package com.asia.viblo.view.asyncTask

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import com.asia.viblo.model.Post
import com.asia.viblo.model.keyMaxPage
import com.asia.viblo.model.keyPagePresent
import com.asia.viblo.utils.SharedPrefs
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Created by FRAMGIA\vu.tuan.anh on 30/10/2017.
 */
val baseUrlNewest = "https://viblo.asia"
val baseUrlSeries = "https://viblo.asia/series"
val baseUrlEditorsChoice = "https://viblo.asia/editors-choice"
val baseUrlTrending = "https://viblo.asia/trending"
val baseUrlVideos = "https://viblo.asia/videos"
val cssQueryFeaturedArticles = "div#__nuxt > div#app-container > div#main-content > div >" +
        " div.container > div.row > div.col-lg-9 > div > div > div.card"
val cssQueryAvatar = "div.card-block > figure.post-author-avatar > a > img"
val cssQueryName = "div.card-block > div.ml-05 > div.post-header > div.post-meta > a"
val cssQueryTime = "div.card-block > div.ml-05 > div.post-header > div.post-meta > " +
        "div.text-muted > span"
val cssQueryUrl = "div.card-block > div.ml-05 > div.post-header > div.post-title-box > " +
        "h1.post-title-header > a"
val cssQueryScore = "div.card-block > div.ml-05 > div.d-flex > div.points > span"
val cssQueryView = "div.card-block > div.ml-05 > div.d-flex"
val cssQueryPage = "div#__nuxt > div#app-container > div#main-content > div > div.container " +
        "> div.row > div.col-lg-9 > div > div > ul.pagination"

@SuppressLint("StaticFieldLeak")
class LoadPostAsyncTask : AsyncTask<String, Void, List<Post>>() {
    override fun doInBackground(vararg params: String?): List<Post> {
        val topicList: MutableList<Post> = arrayListOf()
        val baseUrl = params[0]
        val page: String = if (params.size == 1) "" else "/?page=" + params[1]
        try {
            val document = Jsoup.connect(baseUrl + page).get()
            val elements = document?.select(cssQueryFeaturedArticles)
            for (element: Element in elements!!) {
                val post = Post()
                val avatarSubject = element.select(cssQueryAvatar).first()
                val nameSubject = element.select(cssQueryName).first()
                val timeSubject = element.select(cssQueryTime).first()
                val urlSubject = element.select(cssQueryUrl).first()
                val scoreSubject = element.select(cssQueryScore).first()
                val postStatusSubject = element.select(cssQueryView).first()
                if (postStatusSubject != null) {
                    val viewSubject = postStatusSubject.getElementsByTag("span")
                    viewSubject.map { it.text() }
                            .forEachIndexed { index, data ->
                                when (index) {
                                    0 -> post.views = data
                                    1 -> post.clips = data
                                    2 -> post.comments = data
                                }
                            }
                }
                post.avatar = avatarSubject?.attr("src")!!
                post.name = nameSubject?.text()!!
                post.time = timeSubject?.text()!!
                post.url = baseUrl + urlSubject?.attr("href")!!
                post.title = urlSubject.text()!!
                if (scoreSubject != null) {
                    post.score = scoreSubject.text()
                }
                topicList.add(post)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        val pageList: MutableList<String> = arrayListOf()
        try {
            val document = Jsoup.connect(baseUrl + page).get()
            val elements = document?.select(cssQueryPage)
            elements!!
                    .map { it.select("li") }
                    .forEach { data ->
                        data.asSequence()
                                .map { it.getElementsByTag("a").text() }
                                .filterNotTo(pageList) { TextUtils.isEmpty(it) }
                    }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        if (pageList.isNotEmpty()) {
            SharedPrefs.instance.put(keyMaxPage, pageList.last())
        }
        SharedPrefs.instance.put(keyPagePresent, if (params.size == 1) "1" else params[1])
        Log.d("TAG", "topList.size = " + topicList.size)
        return topicList
    }
}