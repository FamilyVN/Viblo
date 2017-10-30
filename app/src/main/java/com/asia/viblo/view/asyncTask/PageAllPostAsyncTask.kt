package com.asia.viblo.view.asyncTask

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import org.jsoup.Jsoup

/**
 * Created by FRAMGIA\vu.tuan.anh on 30/10/2017.
 */
val cssQueryPage = "div#__nuxt > div#app-container > div#main-content > div > div.container " +
        "> div.row > div.col-lg-9 > div > div > ul.pagination"

@SuppressLint("StaticFieldLeak")
class PageAllPostAsyncTask : AsyncTask<String, Void, List<String>>() {
    override fun doInBackground(vararg params: String?): List<String> {
        val pageList: MutableList<String> = arrayListOf()
        val page: String = if (params.isEmpty()) "" else "/?page=" + params[0]
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
        val index = pageList.indexOf("...")
//        val iterate = pageList.iterator()
//        while (iterate.hasNext()) {
//            val oldValue = iterate.hasNext()
//            if (index == 2) {
//                iterate.remove()
//            }
//        }
        Log.d("TAG", "index = " + index)
        Log.d("TAG", "pageList.size = " + pageList.size)
        return pageList
    }
}