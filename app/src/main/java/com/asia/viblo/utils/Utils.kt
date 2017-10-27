package com.asia.viblo.utils

import android.widget.ImageView
import com.asia.viblo.R
import com.squareup.picasso.Picasso

/**
 * Created by FRAMGIA\vu.tuan.anh on 27/10/2017.
 */
fun loadAvatar(imageView: ImageView, url: String) {
    Picasso.with(imageView.context)
            .load(url)
//            .placeholder(R.drawable.logo_w)
            .resize(R.dimen.size_70, R.dimen.size_70)
            .centerCrop()
            .into(imageView)
}