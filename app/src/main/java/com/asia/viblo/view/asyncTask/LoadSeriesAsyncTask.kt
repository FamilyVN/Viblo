package com.asia.viblo.view.asyncTask

import android.os.AsyncTask
import com.asia.viblo.model.series.SeriesDetail
import com.asia.viblo.view.activity.series.OnUpdateSeriesDetail
import org.jsoup.Jsoup

/**
 * Created by TuanAnh on 11/19/2017.
 */
val cssQuerySeriesDetail = "div#__nuxt > div#app-container > div#main-content > " +
        "div.series-view > div > div.row > div.col-xl-9 > div.series-content-wrapper"
val cssQuerySeriesAvatar = "div.content-author > a"

class LoadSeriesAsyncTask(onUpdateSeriesDetail: OnUpdateSeriesDetail) : AsyncTask<String, Void, SeriesDetail>() {
    private val mOnUpdateSeriesDetail: OnUpdateSeriesDetail = onUpdateSeriesDetail
    override fun doInBackground(vararg params: String?): SeriesDetail {
        val seriesDetail = SeriesDetail()
        val baseUrl = params[0]
        try {
            val document = Jsoup.connect(baseUrl).get()
            val body = document.body()
            val element = body.select(cssQuerySeriesDetail)
            seriesDetail.avatar = element.select(cssQuerySeriesAvatar).attr("img")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return seriesDetail
    }

    override fun onPostExecute(result: SeriesDetail?) {
        super.onPostExecute(result)
        mOnUpdateSeriesDetail.onUpdateSeriesDetail(result)
    }
}