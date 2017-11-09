package com.asia.viblo.view.activity.home

import com.asia.viblo.model.BaseModel

/**
 * Created by FRAMGIA\vu.tuan.anh on 01/11/2017.
 */
interface OnClickDetail {
    fun onOpenPostDetail(postUrl: String)
    fun onOpenAuthor(baseModel: BaseModel)
}