package com.asia.viblo.model.post

/**
 * Created by FRAMGIA\vu.tuan.anh on 01/11/2017.
 */
open class PostDetail : Post() {
    var publishingDate: String = ""
    var data: MutableList<String> = arrayListOf()
}