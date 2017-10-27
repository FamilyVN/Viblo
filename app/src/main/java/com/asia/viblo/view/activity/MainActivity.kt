package com.asia.viblo.view.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.asia.viblo.R
import com.asia.viblo.model.Post
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class MainActivity : AppCompatActivity() {
    val baseUrl = "https://viblo.asia"
    val cssQueryFeaturedArticles = "div#__nuxt > div#app-container > div#main-content > div > " +
            "div.container > div.row > div.col-lg-9 > div > div > div.card"
    val cssQueryAvatar = "div.card-block > figure.post-author-avatar > a > img"
    val cssQueryName = "div.card-block > div.ml-05 > div.post-header > div.post-meta > a"
    val cssQueryTime = "div.card-block > div.ml-05 > div.post-header > div.post-meta > " +
            "div.text-muted > span"
    val cssQueryUrl = "div.card-block > div.ml-05 > div.post-header > div.post-title-box > " +
            "h1.post-title-header > a"
    val cssQueryScore = "div.card-block > div.ml-05 > div.d-flex > div.points > span"
    val cssQueryView = "div.card-block > div.ml-05 > div.d-flex"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NewPostAsyncTask().execute()
    }

    @SuppressLint("StaticFieldLeak")
    inner class NewPostAsyncTask : AsyncTask<String, Void, List<Post>>() {
        override fun doInBackground(vararg params: String?): List<Post> {
            val topicList: MutableList<Post> = arrayListOf()
            try {
                val document = Jsoup.connect(baseUrl).get()
                val elements = document?.select(cssQueryFeaturedArticles)
                for (element: Element in elements!!) {
                    val post = Post()
                    val avatarSubject = element.select(cssQueryAvatar).first()
                    val nameSubject = element.select(cssQueryName).first()
                    val timeSubject = element.select(cssQueryTime).first()
                    val urlSubject = element.select(cssQueryUrl).first()
                    val scoreSubject = element.select(cssQueryScore).first()
                    val viewSubject = element.select(cssQueryView).first().getElementsByTag("span")
                    post.avatar = avatarSubject.attr("src")
                    post.name = nameSubject.text()
                    post.time = timeSubject.text()
                    post.url = baseUrl + urlSubject.attr("href")
                    post.title = urlSubject.text()
                    post.score = scoreSubject.text()
                    viewSubject.map { it.text() }
                            .forEachIndexed { index, data ->
                                when (index) {
                                    0 -> post.views = data
                                    1 -> post.clips = data
                                    2 -> post.comments = data
                                }
                            }
                    topicList.add(post)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return topicList
        }

        override fun onPostExecute(result: List<Post>?) {
            super.onPostExecute(result)
            for (post: Post in result!!) {
                Log.d("TAG", "size = " + result.size)
            }
        }
    }
}
