package com.asia.viblo.model

import java.io.Serializable

/**
 * Created by FRAMGIA\vu.tuan.anh on 09/11/2017.
 */
open class BaseModel : Serializable {
    var avatar = ""
    var name = ""
    var time = ""
    var authorUrl = ""
    var title = ""
    var score = ""
    var views = ""
}