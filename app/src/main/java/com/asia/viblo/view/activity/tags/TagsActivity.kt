package com.asia.viblo.view.activity.tags

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.asia.viblo.R
import com.asia.viblo.databinding.ActivityTagsBinding
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.constant.extraUrl
import com.asia.viblo.model.tag.TagDetail
import com.asia.viblo.view.activity.BaseActivity
import com.asia.viblo.view.asyncTask.tag.LoadTagsAsyncTask
import com.asia.viblo.view.fragment.OnUpdateData

class TagsActivity : BaseActivity(), OnUpdateData<TagDetail> {
    private lateinit var mBinding: ActivityTagsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tags)
        mBinding.data = TagDetail()
    }

    override fun loadData() {
        super.loadData()
        val baseUrl = baseUrlViblo + intent.getStringExtra(extraUrl)
        LoadTagsAsyncTask(this).execute(baseUrl)
    }

    override fun onUpdateData(data: TagDetail?) {
        mProgressDialog.dismiss()
        mBinding.data = data!!
    }

    override fun onUpdateDataList(dataList: MutableList<TagDetail>?) {
        // nothing
    }
}
