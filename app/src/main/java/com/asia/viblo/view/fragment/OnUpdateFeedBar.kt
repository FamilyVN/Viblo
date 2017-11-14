package com.asia.viblo.view.fragment

import java.io.Serializable

/**
 * Created by FRAMGIA\vu.tuan.anh on 06/11/2017.
 */
interface OnUpdateFeedBar : Serializable {
    fun onUpdateFeedBar(feedBarList: List<String>?)
}