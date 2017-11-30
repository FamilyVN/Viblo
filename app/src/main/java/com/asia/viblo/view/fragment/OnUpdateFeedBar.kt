package com.asia.viblo.view.fragment

import com.asia.viblo.libs.recyclerview.SingleAdapter
import java.io.Serializable

/**
 * Created by FRAMGIA\vu.tuan.anh on 06/11/2017.
 */
interface OnUpdateFeedBar : SingleAdapter.Presenter, Serializable {
    fun onUpdateFeedBar(feedBarList: MutableList<String>?)
}