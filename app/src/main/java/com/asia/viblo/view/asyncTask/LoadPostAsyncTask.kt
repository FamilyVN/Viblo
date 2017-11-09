package com.asia.viblo.view.asyncTask

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.text.TextUtils
import com.asia.viblo.model.baseUrlSeries
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.keyMaxPage
import com.asia.viblo.model.keyPagePresent
import com.asia.viblo.model.post.Post
import com.asia.viblo.utils.SharedPrefs
import com.asia.viblo.view.fragment.post.OnUpdatePostData
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Created by FRAMGIA\vu.tuan.anh on 30/10/2017.
 */
// cssQuery Post
val cssQueryFeaturedArticles = "div#__nuxt > div#app-container > div#main-content > div >" +
        " div.container > div.row > div.col-lg-9 > div > div.card"
val cssQueryFeaturedSeries = "div#__nuxt > div#app-container > div#main-content > div >" +
        " div.container > div.row > div.col-lg-9 > div > div > div.card"
val cssQueryAvatarPost = "div.card-block > figure.post-author-avatar > a > img"
val cssQueryAvatarPostSeries = "div.card-block > a > img"
val cssQueryNamePost = "div.card-block > div.ml-05 > div.post-header > div.post-meta > a"
val cssQueryNamePostSeries = "div.card-block > div.ml-05 > div.series-header > div.series-meta > a"
val cssQueryTimePost = "div.card-block > div.ml-05 > div.post-header > div.post-meta > " +
        "div.text-muted > span"
val cssQueryTimePostSeries = "div.card-block > div.ml-05 > div.series-header > div.series-meta > " +
        "div.text-muted > span"
val cssQueryPostUrl = "div.card-block > div.ml-05 > div.post-header > div.post-title-box > " +
        "h1.post-title-header > a"
val cssQueryPostUrlSeries = "div.card-block > div.ml-05 > div.series-header > " +
        "div.series-title-box > h1.series-title-header > a"
val cssQueryScore = "div.card-block > div.ml-05 > div.d-flex > div.points > span"
val cssQueryView = "div.card-block > div.ml-05 > div.d-flex"
val cssQueryPage = "div#__nuxt > div#app-container > div#main-content > div > div.container " +
        "> div.row > div.col-lg-9 > div > ul.pagination"
val cssQueryPageSeries = "div#__nuxt > div#app-container > div#main-content > div > div.container" +
        " > div.row > div.col-lg-9 > div > ul.pagination"
val cssQueryAuthorUrl = "div.card-block"

@SuppressLint("StaticFieldLeak")
class LoadPostAsyncTask(onUpdatePostData: OnUpdatePostData) : AsyncTask<String, Void, List<Post>>() {
    private val mOnUpdatePostData = onUpdatePostData
    override fun doInBackground(vararg params: String?): List<Post> {
        val postList: MutableList<Post> = arrayListOf()
        val baseUrl = params[0]
        val page: String = if (params.size == 1) "" else getLinkPage(baseUrl, params[1])
        try {
            val document = Jsoup.connect(baseUrl + page).get()
            val elements = document?.select(getCssQuery(baseUrl, TypeQuery.BASE))
            for (element: Element in elements!!) {
                val post = Post()
//                val avatarSubject = element.select(getCssQuery(baseUrl, TypeQuery.AVATAR)).attr("srcset")!!.split(",")
                val avatarSubject = element.select(getCssQuery(baseUrl, TypeQuery.AVATAR)).first()
                val nameSubject = element.select(getCssQuery(baseUrl, TypeQuery.NAME)).first()
                val timeSubject = element.select(getCssQuery(baseUrl, TypeQuery.TIME)).first()
                val urlSubject = element.select(getCssQuery(baseUrl, TypeQuery.URL)).first()
                val authorsUrlSubject = element.select(cssQueryAuthorUrl).first()
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
//                val avatar = avatarSubject[avatarSubject.size - 1]
//                post.avatar = avatar.substring(0, avatar.length - 3)
                post.avatar = avatarSubject?.attr("src")!!
                post.name = nameSubject?.text()!!
                post.time = timeSubject?.text()!!
                post.postUrl = baseUrl + urlSubject?.attr("href")!!
                post.authorUrl = baseUrl + authorsUrlSubject?.getElementsByTag("a")?.first()?.attr("href")
                post.title = urlSubject.text()!!
                if (scoreSubject != null) {
                    post.score = scoreSubject.text()
                }
                postList.add(post)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        val pageList: MutableList<String> = arrayListOf()
        try {
            val document = Jsoup.connect(baseUrl + page).get()
            val elements = document?.select(getCssQuery(baseUrl, TypeQuery.PAGE))
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
        SharedPrefs.instance.put(keyMaxPage, if (pageList.isNotEmpty()) pageList.last() else "0")
        if (params.size == 1) {
            SharedPrefs.instance.put(keyPagePresent, "1")
        }
        return postList
    }

    override fun onPostExecute(result: List<Post>?) {
        super.onPostExecute(result)
        mOnUpdatePostData.onUpdatePostData(result)
    }

    private fun getCssQuery(baseUrl: String?, typeQuery: TypeQuery): String {
        val cssQuery: String
        when (baseUrl) {
            baseUrlSeries -> {
                cssQuery = when (typeQuery) {
                    TypeQuery.PAGE -> cssQueryPageSeries
                    TypeQuery.AVATAR -> cssQueryAvatarPostSeries
                    TypeQuery.NAME -> cssQueryNamePostSeries
                    TypeQuery.TIME -> cssQueryTimePostSeries
                    TypeQuery.URL -> cssQueryPostUrlSeries
                    else -> cssQueryFeaturedSeries
                }
            }
            else -> {
                cssQuery = when (typeQuery) {
                    TypeQuery.PAGE -> cssQueryPage
                    TypeQuery.AVATAR -> cssQueryAvatarPost
                    TypeQuery.NAME -> cssQueryNamePost
                    TypeQuery.TIME -> cssQueryTimePost
                    TypeQuery.URL -> cssQueryPostUrl
                    else -> cssQueryFeaturedArticles
                }
            }
        }
        return cssQuery
    }

    private fun getLinkPage(baseUrl: String?, page: String?): String {
        var pageCheck = page
        try {
            if (page != null) {
                val pageMaxStr = SharedPrefs.instance[keyMaxPage, String::class.java]
                if (!TextUtils.isEmpty(pageMaxStr)) {
                    val pageMax = pageMaxStr.toInt()
                    val pagePresent = page.toInt()
                    if (pagePresent > pageMax) {
                        pageCheck = pageMaxStr
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        if (!TextUtils.isEmpty(pageCheck)) {
            SharedPrefs.instance.put(keyPagePresent, pageCheck)
        }
        return when (baseUrl) {
            baseUrlViblo -> "/?page="
            else -> "?page="
        } + pageCheck
    }
}