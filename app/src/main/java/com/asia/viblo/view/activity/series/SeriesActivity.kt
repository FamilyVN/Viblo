package com.asia.viblo.view.activity.series

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.asia.viblo.R
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.extraUrl
import com.asia.viblo.model.post.Post
import com.asia.viblo.model.series.SeriesDetail
import com.asia.viblo.utils.loadAvatar
import com.asia.viblo.utils.setTags
import com.asia.viblo.view.activity.BaseActivity
import com.asia.viblo.view.adapter.PostAdapter
import com.asia.viblo.view.asyncTask.series.LoadSeriesAsyncTask
import kotlinx.android.synthetic.main.activity_series.*
import kotlinx.android.synthetic.main.include_layout_status.view.*

class SeriesActivity : BaseActivity(), OnUpdateSeriesDetail {
    private lateinit var mPostAdapter: PostAdapter
    private var mPostList = arrayListOf<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)
        initRecyclerContent()
    }

    private fun initRecyclerContent() {
        mPostAdapter = PostAdapter(this, mPostList, this, this, this)
        recyclerContent.adapter = mPostAdapter
        recyclerContent.layoutManager = LinearLayoutManager(this)
    }

    override fun loadData() {
        super.loadData()
        val baseUrl = baseUrlViblo + intent.getStringExtra(extraUrl)
        LoadSeriesAsyncTask(this).execute(baseUrl)
    }

    override fun onUpdateSeriesDetail(seriesDetail: SeriesDetail?) {
        mProgressDialog.dismiss()
        updateViews(seriesDetail)
        updateRecyclerContent(seriesDetail?.seriesList)
    }

    private fun updateRecyclerContent(seriesList: MutableList<Post>?) {
        if (seriesList != null) {
            mPostList.clear()
            mPostList.addAll(seriesList)
            mPostAdapter.notifyDataSetChanged()
        }
    }

    private fun updateViews(seriesDetail: SeriesDetail?) {
        if (!TextUtils.isEmpty(seriesDetail?.avatar)) {
            loadAvatar(imageAvatar, seriesDetail?.avatar!!)
        }
        txtName.text = seriesDetail?.name
        txtPublishingDate.text = seriesDetail?.publishingDate
        txtTitle.text = seriesDetail?.title
        setTags(flowLayout, seriesDetail?.tagList, seriesDetail?.tagUrlList, this)
        if (TextUtils.isEmpty(seriesDetail?.views)) {
            layoutStatus.views.visibility = View.INVISIBLE
            layoutStatus.visibility = View.GONE
        } else {
            layoutStatus.views.visibility = View.VISIBLE
            layoutStatus.views.text = seriesDetail?.views
            layoutStatus.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(seriesDetail?.clips)) {
            layoutStatus.clips.visibility = View.INVISIBLE
        } else {
            layoutStatus.clips.visibility = View.VISIBLE
            layoutStatus.clips.text = seriesDetail?.clips
        }
        if (TextUtils.isEmpty(seriesDetail?.posts)) {
            layoutStatus.posts.visibility = View.INVISIBLE
        } else {
            layoutStatus.posts.visibility = View.VISIBLE
            layoutStatus.posts.text = seriesDetail?.posts
        }
        txtScore.text = seriesDetail?.score
        layoutVote.visibility = if (TextUtils.isEmpty(seriesDetail?.score)) View.GONE else View.VISIBLE
        layoutStatus.score.visibility = View.GONE
        layoutStatus.comments.visibility = View.GONE
        contentHtml.addContentHtml(seriesDetail?.data!!)
    }
}
