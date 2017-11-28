package com.asia.viblo.view.asyncTask.author

import android.os.AsyncTask
import android.text.TextUtils
import com.asia.viblo.model.author.AuthorDetail
import com.asia.viblo.model.post.Post
import com.asia.viblo.utils.getDocument
import com.asia.viblo.view.asyncTask.TypeQuery
import com.asia.viblo.view.fragment.author.OnUpdateAuthorData
import org.jsoup.nodes.Element

/**
 * Created by anhtv on 22/11/2017.
 */
private val cssQueryAuthorPost = "div#__nuxt > div#app-container > div#main-content > " +
        "div.user-page > div.container > div.row > div.col-lg-9 > div.user-activities > " +
        "div.posts-list > div > div.card"
private val cssQueryAuthorClips = "div#__nuxt  > div#app-container > div#main-content > " +
        "div > div.container > div.row > div.col-lg-9 > div > div.card"
private val cssQueryAuthorFollowing = "div#__nuxt  > div#app-container > div#main-content > " +
        "div.user-page > div.container > div.row > div.col-lg-9 > div.user-activities > " +
        "div.posts-list > div > div.block-exhibition > div.col-sm-6"
private val cssQueryAuthorAvatarPost = "div.card-block > figure.post-author-avatar > a > img"
private val cssQueryAuthorAvatarFollowing = "div.d-flex > a > img"
private val cssQueryAuthorPostIsVideo = "div.card-block > div.ml-05 > div.post-header > " +
        "div.post-title-box > h1.post-title-header > span > i"
private val cssQueryAuthorNamePost = "div.card-block > div.ml-05 > div.post-header > " +
        "div.post-meta > a"
private val cssQueryAuthorNameFollowing = "div.d-flex > div.user-info > a"
private val cssQueryAuthorTimePost = "div.card-block > div.ml-05 > div.post-header > " +
        "div.post-meta > div.text-muted > span"
private val cssQueryAuthorUrlPost = "div.card-block > div.ml-05 > div.post-header > " +
        "div.post-title-box > h1.post-title-header > a"
private val cssQueryAuthorStatusPost = "div.card-block > div.ml-05 > div.d-flex"
private val cssQueryAuthorScorePost = "div.card-block > div.ml-05 > div.d-flex > div.points > span"
private val cssQueryAuthorAuthorUrlPost = "div.card-block"
private val cssQueryAuthorAuthorUrlFollowing = "div.d-flex"
private val cssQueryAuthorTagPost = "div.card-block > div.ml-05 > div.post-header > " +
        "div.post-title-box > div.tags > a"
private val cssQueryAuthorContentStats = "div.d-flex > div.user-info >" +
        "div.user-stats"

class LoadAuthorAsyncTask(onUpdateAuthorData: OnUpdateAuthorData) : AsyncTask<String, Void, AuthorDetail>() {
    private val mOnUpdateAuthorData: OnUpdateAuthorData = onUpdateAuthorData
    override fun doInBackground(vararg params: String?): AuthorDetail {
        val authorDetail = AuthorDetail()
        val postList: MutableList<Post> = arrayListOf()
        val baseUrl = params[0]
        val page: String = if (params.size == 1) "" else "?page=" + params[1]
        try {
            val document = getDocument(baseUrl + page)
            val elements = document?.select(getCssQuery(baseUrl, TypeQuery.BASE))
            for (element: Element in elements!!) {
                val post = Post()
//                val avatarSubject = element.select(getCssQuery(baseUrl, TypeQuery.AVATAR)).attr("srcset")!!.split(",")
                val avatarSubject = element.select(getCssQuery(baseUrl, TypeQuery.AVATAR)).first()
                val nameSubject = element.select(getCssQuery(baseUrl, TypeQuery.NAME)).first()
                val timeSubject = element.select(getCssQuery(baseUrl, TypeQuery.TIME)).first()
                val urlSubject = element.select(getCssQuery(baseUrl, TypeQuery.URL)).first()
                val authorsUrlSubject = element.select(getCssQuery(baseUrl, TypeQuery.AUTHOR)).first()
                val scoreSubject = element.select(cssQueryAuthorScorePost).first()
                val postStatusSubject = element.select(cssQueryAuthorStatusPost).first()
                val tagSubject = element.select(cssQueryAuthorTagPost)
                val videoSubject = element.select(cssQueryAuthorPostIsVideo)
                if (postStatusSubject != null) {
                    val viewSubject = postStatusSubject.getElementsByTag("span")
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
                if (timeSubject != null) {
                    post.time = timeSubject.text()!!
                }
                if (urlSubject != null) {
                    post.postUrl = urlSubject.attr("href")!!
                }
                if (authorsUrlSubject != null) {
                    post.authorUrl = authorsUrlSubject.getElementsByTag("a")?.first()?.attr("href")!!
                }
                if (urlSubject != null) {
                    post.title = urlSubject.text()!!
                }
                if (scoreSubject != null) {
                    post.score = scoreSubject.text()
                }
                tagSubject
                        .filterNot { TextUtils.isEmpty(it.text()) }
                        .forEach {
                            post.tagList.add(it.text())
                            post.tagUrlList.add(it.attr("href"))
                        }
                // stats
                val statsSubject = element.select(cssQueryAuthorContentStats).first()
                if (statsSubject != null) {
                    val viewSubject = statsSubject.getElementsByTag("span")
                    viewSubject.map {
                        if (TextUtils.isEmpty(it.text())) "0" else it.text()
                    }
                            .forEachIndexed { index, data ->
                                when (index) {
                                    0 -> post.reputation = data
                                    1 -> post.followers = data
                                    2 -> post.post = data
                                }
                            }
                }
                //
                post.isVideo = TextUtils.equals(videoSubject.attr("aria-hidden"), "true")
                postList.add(post)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        authorDetail.postList.addAll(postList)
        return authorDetail
    }

    override fun onPostExecute(result: AuthorDetail?) {
        super.onPostExecute(result)
        mOnUpdateAuthorData.onUpdateAuthorData(result)
    }

    private fun getCssQuery(baseUrl: String?, typeQuery: TypeQuery): String {
        return when {
            baseUrl?.contains("/clips/posts")!! -> {
                when (typeQuery) {
                    TypeQuery.AVATAR -> cssQueryAuthorAvatarPost
                    TypeQuery.NAME -> cssQueryAuthorNamePost
                    TypeQuery.TIME -> cssQueryAuthorTimePost
                    TypeQuery.URL -> cssQueryAuthorUrlPost
                    TypeQuery.TAG -> cssQueryAuthorTagPost
                    TypeQuery.AUTHOR -> cssQueryAuthorAuthorUrlPost
                    else -> cssQueryAuthorClips
                }
            }
            baseUrl.contains("/following") -> {
                when (typeQuery) {
                    TypeQuery.AVATAR -> cssQueryAuthorAvatarFollowing
                    TypeQuery.NAME -> cssQueryAuthorNameFollowing
                    TypeQuery.AUTHOR -> cssQueryAuthorAuthorUrlFollowing
                    else -> cssQueryAuthorFollowing
                }
            }
            else -> {
                when (typeQuery) {
                    TypeQuery.AVATAR -> cssQueryAuthorAvatarPost
                    TypeQuery.NAME -> cssQueryAuthorNamePost
                    TypeQuery.TIME -> cssQueryAuthorTimePost
                    TypeQuery.URL -> cssQueryAuthorUrlPost
                    TypeQuery.TAG -> cssQueryAuthorTagPost
                    TypeQuery.AUTHOR -> cssQueryAuthorAuthorUrlPost
                    else -> cssQueryAuthorPost
                }
            }
        }
    }
}