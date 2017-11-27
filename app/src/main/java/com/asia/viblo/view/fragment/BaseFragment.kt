package com.asia.viblo.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import com.asia.viblo.R
import com.asia.viblo.model.BaseModel
import com.asia.viblo.model.keyMaxPage
import com.asia.viblo.model.keyPagePresent
import com.asia.viblo.utils.SharedPrefs
import com.asia.viblo.utils.checkErrorNetwork
import com.asia.viblo.utils.showProgressDialog
import com.asia.viblo.view.activity.home.OnClickComment
import com.asia.viblo.view.activity.home.OnClickDetail
import com.asia.viblo.view.activity.home.OnClickTag
import com.asia.viblo.view.asyncTask.feedbar.FeedBarAsyncTask
import com.asia.viblo.view.custom.DialogSelectPage
import kotlinx.android.synthetic.main.dialog_select_page.view.*
import kotlinx.android.synthetic.main.include_layout_next_back_page.*

/**
 * Created by FRAMGIA\vu.tuan.anh on 03/11/2017.
 */
abstract class BaseFragment : Fragment(), OnSelectPage, OnUpdateFeedBar,
        OnClickTag, OnClickDetail, OnClickComment {
    lateinit var mProgressDialog: Dialog
    var mPosition = 0
    private var mAlertDialog: AlertDialog? = null
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProgressDialog = showProgressDialog(context)
        initListener()
        initSpinner()
    }

    open fun initListener() {
        textPageNext.setOnClickListener {
            val pageNext = SharedPrefs.instance[keyPagePresent, String::class.java].toInt() + 1
            loadData(getLink(mPosition), pageNext.toString())
        }
        textPageBack.setOnClickListener {
            val pageBack = SharedPrefs.instance[keyPagePresent, String::class.java].toInt() - 1
            loadData(getLink(mPosition), pageBack.toString())
        }
        textPagePresent.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val dialogSelectPage = DialogSelectPage(context, null, this) as LinearLayout
            dialogSelectPage.txtTitle.text = getString(R.string.text_dialog_title)
            dialogSelectPage.txtMessage.text = String.format(
                    getString(R.string.text_dialog_message, "1", SharedPrefs.instance[keyMaxPage, String::class.java]))
            dialogSelectPage.editPage.setText(SharedPrefs.instance[keyPagePresent, String::class.java])
            builder.setView(dialogSelectPage)
            mAlertDialog = builder.show()
        }
    }

    open fun initSpinner() {
        initSpinner(null)
    }

    open fun initSpinner(params: String?) {
        if (!checkErrorNetwork(context)) return
        showProgressDialog()
        if (TextUtils.isEmpty(params)) {
            FeedBarAsyncTask(this).execute(getLink(mPosition))
        } else {
            FeedBarAsyncTask(this).execute(getLink(mPosition), params)
        }
    }

    open fun showProgressDialog() {
        if (mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
        mProgressDialog.show()
    }

    open fun loadData(url: String) {
        loadData(url, "")
    }

    open fun loadData(url: String, page: String) {
        if (!checkErrorNetwork(context)) return
        showProgressDialog()
    }

    override fun onSelectPage(pageSelected: String) {
        mAlertDialog?.dismiss()
        loadData(getLink(mPosition), pageSelected)
    }

    override fun onCancel() {
        mAlertDialog?.dismiss()
    }

    override fun onUpdateFeedBar(feedBarList: List<String>?) {

    }

    open fun getPagePresent(pagePresentStr: String, pageMaxStr: String): String {
        return pagePresentStr + "/" + pageMaxStr
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

    abstract fun getLink(type: Int): String
}