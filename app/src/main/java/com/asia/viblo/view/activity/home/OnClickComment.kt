package com.asia.viblo.view.activity.home

import com.asia.viblo.libs.recyclerview.SingleAdapter
import java.io.Serializable

/**
 * Created by anhtv on 28/11/2017.
 */
interface OnClickComment : SingleAdapter.Presenter, Serializable {
    fun onOpenAuthorComment(authorUrl: String)
    fun onOpenAllAuthorComment(authorUrlList: MutableList<String>?)
}