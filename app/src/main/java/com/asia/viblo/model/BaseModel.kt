package com.asia.viblo.model

import android.databinding.BaseObservable
import java.io.Serializable

/**
 * Created by FRAMGIA\vu.tuan.anh on 09/11/2017.
 */
open class BaseModel : BaseObservable(), Serializable {
    var avatar = ""
    var name = ""
    var time = ""
    var authorUrl = ""
    var title = ""
    var score = ""
    var views = ""
    var comments = ""
    var numberMore = ""
    var tagList: MutableList<String> = arrayListOf()
    var commentList: MutableList<String> = arrayListOf()
    var tagUrlList: MutableList<String> = arrayListOf()
    var dataNull = ""
}