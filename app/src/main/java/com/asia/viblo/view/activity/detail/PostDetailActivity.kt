package com.asia.viblo.view.activity.detail

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.asia.viblo.R
import com.asia.viblo.model.BaseModel
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.extraData
import com.asia.viblo.model.extraUrl
import com.asia.viblo.model.post.PostDetail
import com.asia.viblo.utils.loadAvatar
import com.asia.viblo.utils.setTags
import com.asia.viblo.view.activity.BaseActivity
import com.asia.viblo.view.activity.author.AuthorActivity
import com.asia.viblo.view.activity.home.OnClickTag
import com.asia.viblo.view.asyncTask.PostDetailAsyncTask
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.include_layout_views_clips_comments.view.*

class PostDetailActivity : BaseActivity(), OnClickTag, OnUpdatePostDetail {
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
        layoutView.score.visibility = View.INVISIBLE
        //
        setTags(flowLayout, mPostDetail.tags, mPostDetail.tagUrlList, this)
        //
        contentHtml.addContentHtml(mPostDetail.data)
        txtName.setOnClickListener {
            onOpenAuthor(mPostDetail)
        }
        imageAvatar.setOnClickListener {
            onOpenAuthor(mPostDetail)
        }
    }

    private fun onOpenAuthor(baseModel: BaseModel) {
        val intent = Intent(this, AuthorActivity::class.java)
        intent.putExtra(extraData, baseModel)
        startActivity(intent)
    }

    override fun onOpenTag(tagUrl: String) {
        Log.d("TAG.PostDetailActivity", "tagUrl = " + baseUrlViblo + tagUrl)
        Toast.makeText(this, tagUrl, Toast.LENGTH_SHORT).show()
    }

    override fun onUpdatePostDetail(postDetail: PostDetail?) {
        mProgressDialog.dismiss()
        mPostDetail = postDetail!!
        updateView()
    }
}
