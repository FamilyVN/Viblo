package com.asia.viblo.view.activity.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.asia.viblo.R
import com.asia.viblo.model.extraUrl
import com.asia.viblo.model.post.PostDetail
import com.asia.viblo.utils.loadAvatar
import com.asia.viblo.view.asyncTask.PostDetailAsyncTask
import kotlinx.android.synthetic.main.activity_post_detail.*

class PostDetailActivity : AppCompatActivity() {
    private var mPostDetail = PostDetail()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        loadData()
        updateView()
    }

    private fun loadData() {
        mPostDetail = PostDetailAsyncTask().execute(intent.getStringExtra(extraUrl)).get()
    }

    private fun updateView() {
        loadAvatar(imageAvatar, mPostDetail.avatar)
        txtName.text = mPostDetail.name
        txtTime.text = mPostDetail.time
        txtTitle.text = mPostDetail.title
    }
}
