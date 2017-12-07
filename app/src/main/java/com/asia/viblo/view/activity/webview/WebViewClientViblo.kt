package com.asia.viblo.view.activity.webview

import android.app.Dialog
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * Created by FRAMGIA\vu.tuan.anh on 07/12/2017.
 */
class WebViewClientViblo(progressDialog: Dialog?) : WebViewClient() {
    private var mProgressDialog: Dialog? = progressDialog
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        mProgressDialog?.dismiss()
    }
}