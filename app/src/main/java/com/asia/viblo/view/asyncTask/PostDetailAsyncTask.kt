package com.asia.viblo.view.asyncTask

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.text.TextUtils
import com.asia.viblo.model.post.PostDetail
import org.jsoup.Jsoup

/**
 * Created by FRAMGIA\vu.tuan.anh on 01/11/2017.
 */
val cssQueryDetail = "div#__nuxt > div#app-container > div#main-content > div > div > div" +
        ".container > div.row > div.col-xl-9 > div.post-content-wrapper"
private val cssQueryAvatar = "div.post-meta > div.content-author > a > img"
private val cssQueryHeader = "div.post-meta > div.content-author > div.mw-0"
private val cssQueryName = "div.text-bold > div.overflow-hidden > a"
private val cssQueryTime = "div.text-muted > span"
private val cssQueryTitle = "div.post-meta > h1"
val cssQueryPublishingDate = "div.post-meta > div.d-flex > div.meta-d1 > div.publishing-date"
val cssQueryDetailTagDefault = "div.post-meta > div.tags > a"
val cssQueryDetailTag = "div#__nuxt > div#app-container > div#main-content > div > div > div > " +
        "div.container > div.row > div.col-xl-9 > div.tags > a"

@SuppressLint("StaticFieldLeak")
class PostDetailAsyncTask : AsyncTask<String, Void, PostDetail>() {
    override fun doInBackground(vararg params: String?): PostDetail {
        val postDetail = PostDetail()
        val baseUrl = params[0]
        try {
            val document = Jsoup.connect(baseUrl).get()
            val body = document.body()
            val element = body.select(cssQueryDetail)
            postDetail.avatar = element.select(cssQueryAvatar).attr("src")
            val elementHeader = element.select(cssQueryHeader)
            postDetail.name = elementHeader.select(cssQueryName).text()
            postDetail.time = elementHeader.select(cssQueryTime).text()
            postDetail.title = element.select(cssQueryTitle).text()
            postDetail.publishingDate = element.select(cssQueryPublishingDate).text()
            val tagSubject = if (TextUtils.isEmpty(postDetail.name)) {
                body.select(cssQueryDetailTag)
            } else {
                element.select(cssQueryDetailTagDefault)
            }
            tagSubject
                    .filterNot { TextUtils.isEmpty(it.text()) }
                    .forEach {
                        postDetail.tags.add(it.text())
                        postDetail.tagUrlList.add(it.attr("href"))
                    }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return postDetail
    }
}