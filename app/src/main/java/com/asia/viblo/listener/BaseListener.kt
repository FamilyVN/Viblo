package com.asia.viblo.listener

import android.app.Activity
import android.content.Intent
import com.asia.viblo.model.BaseModel
import com.asia.viblo.model.constant.extraData
import com.asia.viblo.model.constant.extraUrl
import com.asia.viblo.view.activity.author.AuthorActivity
import com.asia.viblo.view.activity.home.OnClickComment
import com.asia.viblo.view.activity.home.OnClickDetail
import com.asia.viblo.view.activity.home.OnClickTag
import com.asia.viblo.view.activity.postdetail.PostDetailActivity
import com.asia.viblo.view.activity.questions.QuestionsActivity
import com.asia.viblo.view.activity.series.SeriesActivity
import com.asia.viblo.view.activity.tags.TagsActivity
import com.asia.viblo.view.activity.webview.WebViewActivity

/**
 * Created by FRAMGIA\vu.tuan.anh on 30/11/2017.
 */
class BaseListener(activity: Activity) : OnClickTag, OnClickDetail, OnClickComment {
    private val mActivity: Activity = activity
    override fun onOpenTag(tagUrl: String) {
        val intent = Intent(mActivity, TagsActivity::class.java)
        intent.putExtra(extraUrl, tagUrl)
        mActivity.startActivity(intent)
    }

    override fun onOpenAuthorComment(authorUrl: String) {
    }

    override fun onOpenAllAuthorComment(authorUrlList: MutableList<String>?) {
    }

    override fun onOpenDetail(url: String, isVideo: Boolean) {
        val intent = when {
            url.contains("/s/") -> Intent(mActivity, SeriesActivity::class.java)
            url.contains("/q/") -> Intent(mActivity, QuestionsActivity::class.java)
            isVideo -> Intent(mActivity, WebViewActivity::class.java)
            else -> Intent(mActivity, PostDetailActivity::class.java)
        }
        intent.putExtra(extraUrl, url)
        mActivity.startActivity(intent)
    }

    override fun onOpenAuthor(baseModel: BaseModel) {
        val intent = Intent(mActivity, AuthorActivity::class.java)
        intent.putExtra(extraData, baseModel)
        mActivity.startActivity(intent)
    }
}