package com.asia.viblo.view.activity.home

import com.asia.viblo.libs.recyclerview.SingleAdapter
import java.io.Serializable

/**
 * Created by anhtv on 13/11/2017.
 */
interface OnClickTag : SingleAdapter.Presenter, Serializable {
    fun onOpenTag(tagUrl: String)
}