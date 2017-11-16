package com.asia.viblo.utils

import android.databinding.BindingAdapter
import android.text.TextUtils
import android.view.View

/**
 * Created by FRAMGIA\vu.tuan.anh on 16/11/2017.
 */
@BindingAdapter("visibility")
fun setVisibility(view: View, vararg text: String) {
    val isEmpty = text.any { TextUtils.isEmpty(it.trim()) }
    if (isEmpty) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}