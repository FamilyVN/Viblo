package com.asia.viblo.view.asyncTask

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.text.TextUtils
import com.asia.viblo.model.post.PostDetail
import com.asia.viblo.view.activity.detail.OnUpdatePostDetail
import org.jsoup.Jsoup

/**
 * Created by FRAMGIA\vu.tuan.anh on 01/11/2017.
 */
private val cssQueryPostDetail = "div#__nuxt > div#app-container > div#main-content > div > div > " +
        "div.container > div.row > div.col-xl-9 > div.post-content-wrapper"
private val cssQueryPostDetailVideo = "div#__nuxt > div#app-container > div#main-content > " +
        "div > div > div"
private val cssQueryPostDetailAnnouncement = "div#__nuxt > div#app-container > div#main-content >" +
        " div.container > div.row > div.col-lg-9 > div.support-article"
private val cssQueryPostDetailAvatar = "div.post-meta > div.content-author > a > img"
private val cssQueryPostDetailHeader = "div.post-meta > div.content-author > div.mw-0"
private val cssQueryPostDetailName = "div.text-bold > div.overflow-hidden > a"
private val cssQueryPostDetailTime = "div.text-muted > span"
private val cssQueryPostDetailTitle = "div.post-meta > h1"
private val cssQueryPostDetailTitleVideo = "div.container > div.row > div.col-12 > h1"
private val cssQueryPostDetailTitleAnnouncement = "h1"
private val cssQueryPostDetailPublishingDate = "div.post-meta > div.d-flex > div.meta-d1 > " +
        "div.publishing-date"
private val cssQueryPostDetailPublishingDateVideo = "div.container > div.row > div.col-xl-9 > " +
        "div.d-flex > div.meta-d1 > div.publishing-date"
private val cssQueryPostDetailTagDefault = "div.post-meta > div.tags > a"
private val cssQueryPostDetailTag = "div#__nuxt > div#app-container > div#main-content > div > " +
        "div > div > div.container > div.row > div.col-xl-9 > div.tags > a"
private val cssQueryPostDetailStatus = "div.post-meta > div.d-flex > div.meta-d2 > div.post-stats > span"
private val cssQueryPostDetailStatusVideo = "div.container > div.row > div.col-xl-9 > " +
        "div.d-flex > div.meta-d2 > div.post-stats > span"
private val cssQueryPostDetailData = "div.md-contents"

@SuppressLint("StaticFieldLeak")
class PostDetailAsyncTask(onUpdatePostDetail: OnUpdatePostDetail) : AsyncTask<String, Void, PostDetail>() {
    private val mOnUpdatePostDetail = onUpdatePostDetail
    override fun doInBackground(vararg params: String?): PostDetail {
        val postDetail = PostDetail()
        val baseUrl = params[0]
        try {
            val document = Jsoup.connect(baseUrl).get()
            val body = document.body()
            val element = body.select(getCssQuery(baseUrl, TypeQuery.BASE))
            postDetail.avatar = element.select(cssQueryPostDetailAvatar).attr("src")
            val elementHeader = element.select(cssQueryPostDetailHeader)
            postDetail.name = elementHeader.select(cssQueryPostDetailName).text()
            postDetail.time = elementHeader.select(cssQueryPostDetailTime).text()
            postDetail.title = element.select(getCssQuery(baseUrl, TypeQuery.TITLE)).text()
            postDetail.publishingDate = element.select(getCssQuery(baseUrl, TypeQuery.PUBLISHING_DATE)).text()
            val tagSubject = if (TextUtils.isEmpty(postDetail.name)) {
                body.select(cssQueryPostDetailTag)
            } else {
                element.select(cssQueryPostDetailTagDefault)
            }
            tagSubject
                    .filterNot { TextUtils.isEmpty(it.text()) }
                    .forEach {
                        postDetail.tags.add(it.text())
                        postDetail.tagUrlList.add(it.attr("href"))
                    }
            // status
            val statusSubject = element.select(getCssQuery(baseUrl, TypeQuery.STATUS))
            statusSubject?.map { it.text() }?.forEachIndexed { index, data ->
                when (index) {
                    0 -> postDetail.views = data
                    1 -> postDetail.clips = data
                    2 -> postDetail.comments = data
                }
            }
            // body
            val dataSubject = element.select(cssQueryPostDetailData)
            dataSubject?.mapNotNull { it.childNodes() }?.flatMap { it }?.forEach { postDetail.data.add(it.outerHtml()) }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return postDetail
    }

    override fun onPostExecute(result: PostDetail?) {
        super.onPostExecute(result)
        mOnUpdatePostDetail.onUpdatePostDetail(result)
    }

    private fun getCssQuery(baseUrl: String?, typeQuery: TypeQuery): String {
        val cssQuery: String
        if (baseUrl?.contains("/announcements/")!!) {
            cssQuery = when (typeQuery) {
                TypeQuery.BASE -> cssQueryPostDetailAnnouncement
                TypeQuery.TITLE -> cssQueryPostDetailTitleAnnouncement
                else -> cssQueryPostDetailAnnouncement
            }
        } else {
            cssQuery =
//            if (mIsVideo) {
//                when (typeQuery) {
//                    TypeQuery.BASE -> cssQueryPostDetailVideo
//                    TypeQuery.TITLE -> cssQueryPostDetailTitleVideo
//                    TypeQuery.PUBLISHING_DATE -> cssQueryPostDetailPublishingDateVideo
//                    TypeQuery.STATUS -> cssQueryPostDetailStatusVideo
//                    else -> cssQueryPostDetail
//                }
//            } else {
                    when (typeQuery) {
                        TypeQuery.BASE -> cssQueryPostDetail
                        TypeQuery.TITLE -> cssQueryPostDetailTitle
                        TypeQuery.PUBLISHING_DATE -> cssQueryPostDetailPublishingDate
                        TypeQuery.STATUS -> cssQueryPostDetailStatus
                        else -> cssQueryPostDetail
                    }
//            }
        }
        return cssQuery
    }
}