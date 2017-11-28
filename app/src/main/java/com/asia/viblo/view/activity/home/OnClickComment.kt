package com.asia.viblo.view.activity.home

import java.io.Serializable

/**
 * Created by anhtv on 28/11/2017.
 */
interface OnClickComment : Serializable {
    fun onOpenAuthorComment(authorUrl: String)
    fun onOpenAllAuthorComment(authorUrlList: MutableList<String>?)
}