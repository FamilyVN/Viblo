package com.asia.viblo.utils

import android.support.annotation.DimenRes
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.asia.viblo.App
import com.asia.viblo.R
import com.asia.viblo.view.custom.FlowLayout
import com.squareup.picasso.Picasso

/**
 * Created by FRAMGIA\vu.tuan.anh on 27/10/2017.
 */
fun loadAvatar(imageView: ImageView, url: String) {
    Picasso.with(imageView.context)
            .load(url)
            .placeholder(R.drawable.ic_avatar_default)
            .into(imageView)
}

fun getSize(@DimenRes dpId: Int): Int {
    return App.self()?.resources?.getDimensionPixelOffset(dpId)!!
}

fun setTags(flowLayout: FlowLayout, tags: MutableList<String>?) {
    if (tags == null) {
        flowLayout.visibility = View.GONE
        return
    }
    flowLayout.visibility = View.VISIBLE
    flowLayout.removeAllViews()
    val context = flowLayout.context
    val layoutInflater = LayoutInflater.from(context)
    for (tag in tags) {
        val tagView = if (TextUtils.equals(tag, "Trending") || TextUtils.equals(tag, "Editors' Choice")) {
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
        flowLayout.addView(tagView)
    }
}
