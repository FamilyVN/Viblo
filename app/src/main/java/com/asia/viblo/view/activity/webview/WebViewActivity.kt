package com.asia.viblo.view.activity.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import com.asia.viblo.R
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.constant.extraUrl
import com.asia.viblo.view.activity.BaseActivity
import com.asia.viblo.view.asyncTask.webview.WebViewAsyncTask
import com.asia.viblo.view.fragment.OnUpdateData
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : BaseActivity(), OnUpdateData<String> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        setupWebView()
        WebViewAsyncTask(this).execute(baseUrlViblo + intent.getStringExtra(extraUrl))
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        CookieSyncManager.createInstance(this)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookie()
        webView.webViewClient = WebViewClientViblo(mProgressDialog)
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
    }

    override fun onUpdateData(data: String?) {
        webView.loadData(data, "text/html; charset=utf-8", "UTF-8")
    }

    override fun onUpdateDataList(dataList: MutableList<String>?) {
        // nothing
    }
}
