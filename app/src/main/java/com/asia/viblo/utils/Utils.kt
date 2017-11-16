package com.asia.viblo.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.support.annotation.DimenRes
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.asia.viblo.App
import com.asia.viblo.R
import com.asia.viblo.view.activity.home.OnClickTag
import com.asia.viblo.view.custom.FlowLayout
import com.squareup.picasso.Picasso
import java.util.regex.Pattern

/**
 * Created by FRAMGIA\vu.tuan.anh on 27/10/2017.
 */
val regex1 = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]"
val regex2 = "\\(?\\b(https://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]"

fun loadAvatar(imageView: ImageView, url: String) {
    Picasso.with(imageView.context)
            .load(url)
            .placeholder(R.drawable.ic_avatar_default)
            .into(imageView)
}

fun getSize(@DimenRes dpId: Int): Int {
    return App.self()?.resources?.getDimensionPixelOffset(dpId)!!
}

fun setTags(flowLayout: FlowLayout, tags: MutableList<String>?, tagUrlList: MutableList<String>?,
            onClickTag: OnClickTag) {
    if (tags == null || tags.size == 0) {
        flowLayout.visibility = View.GONE
        return
    }
    flowLayout.visibility = View.VISIBLE
    flowLayout.removeAllViews()
    val context = flowLayout.context
    val layoutInflater = LayoutInflater.from(context)
    for ((index, tag) in tags.withIndex()) {
        val tagView = if (TextUtils.equals(tag, "Trending")
                || TextUtils.equals(tag, "Editors' Choice")
                || TextUtils.equals(tag, "Announcement")) {
            layoutInflater.inflate(R.layout.item_tag_primary, flowLayout, false) as TextView
        } else {
            layoutInflater.inflate(R.layout.item_tag_default, flowLayout, false) as TextView
        }
        val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.rightMargin = getSize(R.dimen.size_4)
        params.bottomMargin = getSize(R.dimen.size_4)
        tagView.layoutParams = params
        tagView.text = tag
        tagView.setOnClickListener {
            if (tagUrlList != null && tagUrlList.size > 0 && tagUrlList.size >= index) {
                onClickTag.onOpenTag(tagUrlList[index])
            }
        }
        flowLayout.addView(tagView)
    }
}

fun isOnline(context: Context?): Boolean {
    if (context == null) return false
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun checkErrorNetwork(context: Context?): Boolean {
    if (context == null) return false
    if (!isOnline(context.applicationContext)) {
        Toast.makeText(context, R.string.msg_network_error, Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}

fun getUrlListFromString(text: String): MutableList<String> {
    val urlList: MutableList<String> = arrayListOf()
    urlList.addAll(getUrlListFromString(text, regex1))
    urlList.addAll(getUrlListFromString(text, regex2))
    return urlList
}

fun getUrlListFromString(text: String, regex: String): MutableList<String> {
    val urlList: MutableList<String> = arrayListOf()
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(text)
    while (matcher.find()) {
        val urlStr = matcher.group()
        urlList.add(urlStr)
    }
    return urlList
}

fun openBrowser(context: Context?, url: String) {
    if (!TextUtils.isEmpty(url) && context != null) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }
}
