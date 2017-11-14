package com.asia.viblo.view.activity.home

import java.io.Serializable

/**
 * Created by anhtv on 13/11/2017.
 */
interface OnClickTag : Serializable {
    fun onOpenTag(tagUrl: String)
}