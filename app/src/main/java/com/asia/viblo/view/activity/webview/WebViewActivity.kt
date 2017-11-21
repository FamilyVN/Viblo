package com.asia.viblo.view.activity.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import com.asia.viblo.R
import com.asia.viblo.model.baseUrlViblo
import com.asia.viblo.model.extraUrl
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        setupWebView()
        webView.loadUrl(baseUrlViblo + intent.getStringExtra(extraUrl))
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        CookieSyncManager.createInstance(this)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookie()
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
    }
}
