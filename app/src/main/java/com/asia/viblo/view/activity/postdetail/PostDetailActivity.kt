package com.asia.viblo.view.activity.postdetail

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.asia.viblo.R
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.extraUrl
import com.asia.viblo.model.post.PostDetail
import com.asia.viblo.utils.loadAvatar
import com.asia.viblo.utils.setTags
import com.asia.viblo.view.activity.BaseActivity
import com.asia.viblo.view.asyncTask.post.PostDetailAsyncTask
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.include_layout_status.view.*
import kotlinx.android.synthetic.main.include_layout_vote.view.*

class PostDetailActivity : BaseActivity(), OnUpdatePostDetail {
    private var mPostDetail = PostDetail()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
    }

    override fun loadData() {
        super.loadData()
        val baseUrl = baseUrlViblo + intent.getStringExtra(extraUrl)
        PostDetailAsyncTask(this).execute(baseUrl)
    }

    private fun updateView() {
        if (!TextUtils.isEmpty(mPostDetail.avatar)) {
            loadAvatar(imageAvatar, mPostDetail.avatar)
            imageAvatar.visibility = View.VISIBLE
        } else {
            imageAvatar.visibility = View.GONE
        }
        txtName.text = mPostDetail.name
        relativeHeader?.visibility = if (TextUtils.isEmpty(mPostDetail.name)) View.GONE else View.VISIBLE
        txtTime.text = mPostDetail.time
        txtTitle.text = mPostDetail.title
        txtPublishingDate.text = mPostDetail.publishingDate
        txtPublishingDate.visibility = if (TextUtils.isEmpty(mPostDetail.publishingDate)) View.GONE else View.VISIBLE
        if (TextUtils.isEmpty(mPostDetail.views)) {
            layoutView.views.visibility = View.INVISIBLE
            layoutView.visibility = View.GONE
        } else {
            layoutView.views.visibility = View.VISIBLE
            layoutView.views.text = mPostDetail.views
            layoutView.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(mPostDetail.clips)) {
            layoutView.clips.visibility = View.INVISIBLE
        } else {
            layoutView.clips.visibility = View.VISIBLE
            layoutView.clips.text = mPostDetail.clips
        }
        if (TextUtils.isEmpty(mPostDetail.comments)) {
            layoutView.comments.visibility = View.INVISIBLE
        } else {
            layoutView.comments.visibility = View.VISIBLE
            layoutView.comments.text = mPostDetail.comments
        }
        if (TextUtils.isEmpty(mPostDetail.score)) {
            layoutVote.visibility = View.GONE
        } else {
            layoutVote.visibility = View.VISIBLE
            layoutVote.txtScore.text = mPostDetail.score
        }
        layoutView.score.visibility = View.GONE
        //
        setTags(flowLayout, mPostDetail.tagList, mPostDetail.tagUrlList, this)
        //
        contentHtml.addContentHtml(mPostDetail.data)
        txtName.setOnClickListener {
            onOpenAuthor(mPostDetail)
        }
        imageAvatar.setOnClickListener {
            onOpenAuthor(mPostDetail)
        }
    }

    override fun onUpdatePostDetail(postDetail: PostDetail?) {
        mProgressDialog.dismiss()
        mPostDetail = postDetail!!
        updateView()
    }
}
