package com.asia.viblo.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.LinearLayout
import com.asia.viblo.R
import com.asia.viblo.model.keyMaxPage
import com.asia.viblo.model.keyPagePresent
import com.asia.viblo.utils.SharedPrefs
import com.asia.viblo.utils.checkErrorNetwork
import com.asia.viblo.utils.showProgressDialog
import com.asia.viblo.view.asyncTask.FeedBarAsyncTask
import com.asia.viblo.view.custom.DialogSelectPage
import kotlinx.android.synthetic.main.dialog_select_page.view.*
import kotlinx.android.synthetic.main.include_layout_next_back_page.*

/**
 * Created by FRAMGIA\vu.tuan.anh on 03/11/2017.
 */
abstract class BaseFragment : Fragment(), OnSelectPage, OnUpdateFeedBar {
    lateinit var mProgressDialog: Dialog
    var mPosition = 0
    private var mAlertDialog: AlertDialog? = null
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProgressDialog = showProgressDialog(context)
        initListener()
        initSpinner()
    }

    private fun initListener() {
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

    private fun initSpinner() {
        if (!checkErrorNetwork(context)) return
        FeedBarAsyncTask(this).execute(getLink(mPosition))
    }

    open fun loadData(url: String) {
        loadData(url, "")
    }

    open fun loadData(url: String, page: String) {
        if (!checkErrorNetwork(context)) return
        mProgressDialog.show()
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

    abstract fun getLink(type: Int): String
}