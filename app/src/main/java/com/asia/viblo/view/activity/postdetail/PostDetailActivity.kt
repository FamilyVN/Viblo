package com.asia.viblo.view.activity.postdetail

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.asia.viblo.R
import com.asia.viblo.databinding.ActivityPostDetailBinding
import com.asia.viblo.listener.BaseListener
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.constant.extraUrl
import com.asia.viblo.model.post.PostDetail
import com.asia.viblo.view.activity.BaseActivity
import com.asia.viblo.view.asyncTask.post.PostDetailAsyncTask
import com.asia.viblo.view.fragment.OnUpdateData

class PostDetailActivity : BaseActivity(), OnUpdateData<PostDetail> {
    private lateinit var mBinding: ActivityPostDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)
        mBinding.data = PostDetail()
        mBinding.listener = BaseListener(this)
    }

    override fun loadData() {
        super.loadData()
        val baseUrl = baseUrlViblo + intent.getStringExtra(extraUrl)
        PostDetailAsyncTask(this).execute(baseUrl)
    }

    override fun onUpdateData(data: PostDetail?) {
        mProgressDialog.dismiss()
        mBinding.data = data!!
    }

    override fun onUpdateDataList(dataList: MutableList<PostDetail>?) {
        // nothing
    }
}
