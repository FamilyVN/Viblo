package com.asia.viblo.view.activity

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.asia.viblo.model.BaseModel
import com.asia.viblo.utils.showProgressDialog
import com.asia.viblo.view.activity.home.OnClickComment
import com.asia.viblo.view.activity.home.OnClickDetail
import com.asia.viblo.view.activity.home.OnClickTag

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
    }

    override fun onOpenDetail(url: String, isVideo: Boolean) {
    }

    override fun onOpenAuthor(baseModel: BaseModel) {
    }

    override fun onOpenAuthorComment(authorUrl: String) {
    }

    override fun onOpenAllAuthorComment(authorUrlList: MutableList<String>?) {
    }
}