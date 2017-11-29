package com.asia.viblo.view.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.asia.viblo.model.BaseModel
import com.asia.viblo.model.extraData
import com.asia.viblo.model.extraUrl
import com.asia.viblo.utils.showProgressDialog
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
 * Created by TuanAnh on 11/19/2017.
 */
abstract class BaseActivity : AppCompatActivity(), OnClickTag, OnClickDetail, OnClickComment {
    protected lateinit var mProgressDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProgressDialog = showProgressDialog(this)
        loadData()
    }

    open fun loadData() {
        mProgressDialog.show()
    }

    override fun onOpenTag(tagUrl: String) {
        val intent = Intent(this, TagsActivity::class.java)
        intent.putExtra(extraUrl, tagUrl)
        startActivity(intent)
    }

    override fun onOpenDetail(url: String, isVideo: Boolean) {
        val intent = when {
            url.contains("/s/") -> Intent(this, SeriesActivity::class.java)
            url.contains("/q/") -> Intent(this, QuestionsActivity::class.java)
            isVideo -> Intent(this, WebViewActivity::class.java)
            else -> Intent(this, PostDetailActivity::class.java)
        }
        intent.putExtra(extraUrl, url)
        startActivity(intent)
    }

    override fun onOpenAuthor(baseModel: BaseModel) {
        val intent = Intent(this, AuthorActivity::class.java)
        intent.putExtra(extraData, baseModel)
        startActivity(intent)
    }

    override fun onOpenAuthorComment(authorUrl: String) {
    }

    override fun onOpenAllAuthorComment(authorUrlList: MutableList<String>?) {
    }
}