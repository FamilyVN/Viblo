package com.asia.viblo.view.activity.tags

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.asia.viblo.R
import com.asia.viblo.databinding.ActivityTagsBinding
import com.asia.viblo.model.tag.TagDetail

class TagsActivity : AppCompatActivity() {
    private var mTagDetail: TagDetail = TagDetail()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityTagsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tags)
        binding.tagDetail = mTagDetail
    }
}
