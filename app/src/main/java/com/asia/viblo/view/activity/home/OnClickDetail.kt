package com.asia.viblo.view.activity.home

import com.asia.viblo.model.BaseModel
import java.io.Serializable

/**
 * Created by FRAMGIA\vu.tuan.anh on 01/11/2017.
 */
interface OnClickDetail : Serializable {
    fun onOpenPostDetail(postUrl: String)
    fun onOpenAuthor(baseModel: BaseModel)
}