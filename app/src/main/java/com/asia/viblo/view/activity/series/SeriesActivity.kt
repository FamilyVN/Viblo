package com.asia.viblo.view.activity.series

import android.os.Bundle
import com.asia.viblo.R
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.extraUrl
import com.asia.viblo.model.series.SeriesDetail
import com.asia.viblo.view.activity.BaseActivity
import com.asia.viblo.view.asyncTask.LoadSeriesAsyncTask

class SeriesActivity : BaseActivity(), OnUpdateSeriesDetail {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)
    }

    override fun loadData() {
        super.loadData()
        val baseUrl = baseUrlViblo + intent.getStringExtra(extraUrl)
        LoadSeriesAsyncTask(this).execute(baseUrl)
    }

    override fun onUpdateSeriesDetail(seriesDetail: SeriesDetail?) {
        mProgressDialog.dismiss()
        updateViews()
    }

    private fun updateViews() {
    }
}
