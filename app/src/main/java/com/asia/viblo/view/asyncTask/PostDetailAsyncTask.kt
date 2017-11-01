package com.asia.viblo.view.asyncTask

import android.annotation.SuppressLint
import android.os.AsyncTask
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
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return postDetail
    }
}