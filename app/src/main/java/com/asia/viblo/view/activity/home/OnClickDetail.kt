package com.asia.viblo.view.activity.home

import com.asia.viblo.libs.recyclerview.SingleAdapter
import com.asia.viblo.model.BaseModel
import java.io.Serializable

/**
 * Created by FRAMGIA\vu.tuan.anh on 01/11/2017.
 */
interface OnClickDetail : SingleAdapter.Presenter, Serializable {
    fun onOpenDetail(url: String, isVideo: Boolean)
    fun onOpenAuthor(baseModel: BaseModel)
}